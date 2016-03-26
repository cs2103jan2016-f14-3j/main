package parser;


import java.util.ArrayList;
import java.util.Date;
import command.InvalidCommand;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import command.Command;
import main.POMPOM;
import command.AddCommand;
import command.AddRecurringCommand;


public class AddParser extends ArgsParser{
	
	private String itemTitle= null;
	private String itemDescription = null;
	private String itemPriority = null;
	private String itemStatus = null;
	private String itemLabel = null;
	private Date itemStartDate = null;
	private Date itemEndDate = null;
	private DateGroup itemRecurringDateGroup = null;
	private Long itemRecurringPeriod = null;
	private boolean itemIsRecurring = false;
	private String itemEndDateTitle=""; //for use if title is empty.
	private Date exceptStartDate;
	private Date exceptEndDate;
	
	private static final int INDEX_INVALID = -1;
	private static final int INDEX_COMMAND_BEGIN = 0;
	
	
	private final String COMMAND_PREFIX_LABEL = "l:";
	private final String COMMAND_PREFIX_STATUS = "s:";
	private final String COMMAND_PREFIX_PRIORITY = "p:";
	private final String COMMAND_PREFIX_DESCRIPTION = ":";
	private final String STRING_SPACE = " ";
	private final String STRING_EMPTY="";
	private final String DATETIMEPARSER_INDICATOR_START = "start";
	private final String DATETIMEPARSER_INDICATOR_END = "end";
	private final String DATETIMEPARSER_INDICATOR_RECURRING = "recurring";
	private final String DATETIMEPARSER_INDICATOR_EXCEPT = "except";
	
	
	public AddParser(String userCommand){
		super(userCommand);	
		if (this.hasNoArguments){
			invalidArgs();
			return;
		} else {			
			extractDataFromArguments();
		}
	}
	
	/*
	 * This method extracts all data from commandArgumentsString.
	 * commandArgumentsString is always modified every time some
	 * data is extracted from it. 	
	 * 
	 * The order which each argument is extracted is important and
	 * must not be reordered. extractTitle must always be last.
	 * extractRecurring must always be before extractEndDate.
	 * extractPriority, extractStatus and extractLabel must always
	 * be extracted first.
	 * 
	 */
	private void extractDataFromArguments() {
		extractPriority();
		extractStatus();
		extractLabel();
		extractRecurring();
		extractStartDate();
		extractEndDate();	
		extractDescription();	
		extractTitle();
	}
	
	/*
	 * This method extracts the title from commandArgumentsString.
	 * It simply sets whatever that is left in commandArgumentsString
	 * as the title.
	 * 
	 * If commandArgumentsString is empty by this time, then it must
	 * have been extracted by extractEndDate. itemEndDateTitle is created
	 * just for the purpose of getting the title in case the parser extracted
	 * the date wrongly.
	 * 
	 */
	private void extractTitle() {
		if (hasNoTitleToExtract()){
			itemTitle=itemEndDateTitle;
		} else{
			itemTitle = commandArgumentsString.trim();
		}
	}

	
	/*
	 * This method creates the AddCommand() object with the relevant
	 * fields passed into the AddCommand() constructor and then returns
	 * the newly created AddCommand() object.
	 */
	protected Command getCommand(){
		if (itemIsRecurring){
			ArrayList<AddCommand> addCommandList= executeRecurring();
			return checkAndReturnAddRecurringCommand(addCommandList);
		} else{
			return getNonRecurringCommand();
		}
	}
	
	private ArrayList<AddCommand> executeRecurring(){
		
		String recurDateString = itemRecurringDateGroup.getText();
		long newRecurInterval=DateTimeParser.calculateInterval(recurDateString);
		long currentTime = (new Date().getTime());
		long recurInterval=itemRecurringDateGroup.getRecurInterval();

		return getAddCommandArrayList(newRecurInterval, currentTime, recurInterval);
	}
	
	/*
	 * This method returns non-recurring addCommands. It determines which
	 * fields are empty and then determines whether it should be a task
	 * or event and whether it is a floating task or not.
	 */
	protected Command getNonRecurringCommand(){
		Date currentDate = new Date();
		
		if(hasCommandTitleOnly()){
			return new AddCommand(POMPOM.LABEL_TASK, itemTitle, null, null, 
									POMPOM.STATUS_FLOATING, null, null, null);
		} else if (hasCommandTitleAndEndDateOnly()){
			return new AddCommand(POMPOM.LABEL_TASK, itemTitle, null, null, 
									POMPOM.STATUS_ONGOING, null, currentDate, itemEndDate);
		} else if (hasCommandTitleAndStartDateOnly()){
			return new AddCommand(POMPOM.LABEL_TASK, itemTitle, null, null, 
									POMPOM.STATUS_ONGOING, null, itemStartDate, null);	
		} else {
			return new AddCommand(POMPOM.LABEL_EVENT, itemTitle, itemDescription, itemPriority, 
									POMPOM.STATUS_ONGOING, itemLabel, itemStartDate, itemEndDate);
		}
	}
	

	private ArrayList<AddCommand> getAddCommandArrayList(long newRecurInterval, long currentTime,
															long recurInterval) {
		ArrayList<AddCommand> addCommandArrayList = new ArrayList<AddCommand>();
		Date mostRecentEnd=new Date(recurInterval+currentTime);
		while (mostRecentEnd.before(itemEndDate)){
			Date mostRecent= mostRecentEnd;
			recurInterval+= newRecurInterval;
			mostRecentEnd=new Date(recurInterval+currentTime);			
			if(!isSkippableDate(mostRecent)){
				addCommandArrayList.add(getRecurringAddCommand(mostRecent, mostRecentEnd));
			} 
		}
		return addCommandArrayList;
	}

	private AddCommand getRecurringAddCommand(Date mostRecent, Date mostRecentEnd) {
		AddCommand defaultAddCommandFormat=null;
		
		//Take note of the "only". hasCommandTitleAndEndDateONLY() means that all other fields are empty.
		if (hasCommandTitleAndEndDateOnly()){
			 defaultAddCommandFormat = new AddCommand(POMPOM.LABEL_TASK, itemTitle, 
					 									null, null, POMPOM.STATUS_ONGOING, 
					 									null, mostRecent, mostRecentEnd);
		} else if (hasCommandTitleAndEndDate()) {
			defaultAddCommandFormat = new AddCommand(POMPOM.LABEL_EVENT, itemTitle, itemDescription, 
														itemPriority, POMPOM.STATUS_ONGOING,
														itemLabel, mostRecent, mostRecentEnd);
		} else{
			return null;
		}
		return defaultAddCommandFormat;
	}

	private boolean hasCommandTitleAndEndDate() {
		return !isNullTitle() && !isNullEndDate();
	}
	
	private Command checkAndReturnAddRecurringCommand(ArrayList<AddCommand> addCommandList) {
		if (isEmptyAddCommandList(addCommandList)){
			return new InvalidCommand(commandArgumentsString);
		} else{ 
			return new AddRecurringCommand(addCommandList);
		}
	}
	
	private void extractDescription(){
		int indexOfPrefix = commandArgumentsString.indexOf(COMMAND_PREFIX_DESCRIPTION);
		if (isValidIndex(indexOfPrefix)){
			itemDescription = commandArgumentsString.substring(indexOfPrefix+1);
			commandArgumentsString = commandArgumentsString.substring(0, indexOfPrefix);
		}
	}
	
	public void extractPriority(){
		int indexOfPrefix = commandArgumentsString.indexOf(COMMAND_PREFIX_PRIORITY);
		int indexSpace = commandArgumentsString.indexOf(STRING_SPACE,indexOfPrefix);
		if (isValidIndex(indexOfPrefix)){
			itemPriority = extractFieldData(indexOfPrefix, indexSpace, COMMAND_PREFIX_PRIORITY);
			commandArgumentsString = removeFieldFromArgument(indexOfPrefix, indexSpace);
		}
	}
	
	public void extractStatus(){
		int indexOfPrefix = commandArgumentsString.indexOf(COMMAND_PREFIX_STATUS); 
		int indexSpace = commandArgumentsString.indexOf(STRING_SPACE,indexOfPrefix);
		if (isValidIndex(indexOfPrefix)){
			itemStatus = extractFieldData(indexOfPrefix, indexSpace, COMMAND_PREFIX_STATUS);
			commandArgumentsString = removeFieldFromArgument(indexOfPrefix, indexSpace);
		}
	}
	
	public void extractLabel(){
		int indexOfPrefix = commandArgumentsString.indexOf(COMMAND_PREFIX_LABEL);	
		int indexSpace = commandArgumentsString.indexOf(STRING_SPACE,indexOfPrefix);
		if (isValidIndex(indexOfPrefix)){
			itemLabel = extractFieldData(indexOfPrefix, indexSpace, COMMAND_PREFIX_LABEL);
			commandArgumentsString = removeFieldFromArgument(indexOfPrefix, indexSpace);
		}
	}
	
	public void extractStartDate(){
		DateTimeParser startDateTimeParser = new DateTimeParser(DATETIMEPARSER_INDICATOR_START,commandArgumentsString);	
		commandArgumentsString = commandArgumentsString.replace(startDateTimeParser.getString(), STRING_EMPTY);
		itemStartDate=startDateTimeParser.getDate();
		
	}
	
	public void extractEndDate(){
		DateTimeParser endDateTimeParser = new DateTimeParser(DATETIMEPARSER_INDICATOR_END,commandArgumentsString);
		commandArgumentsString = commandArgumentsString.replace(endDateTimeParser.getString(), STRING_EMPTY);
		itemEndDate = endDateTimeParser.getDate();
		itemEndDateTitle= endDateTimeParser.getString();
	}
	
	public void extractRecurring(){
		DateTimeParser recurringTimeParser = new DateTimeParser(DATETIMEPARSER_INDICATOR_RECURRING,commandArgumentsString);
		commandArgumentsString = commandArgumentsString.replace(recurringTimeParser.getString(), STRING_EMPTY);
		itemRecurringDateGroup = recurringTimeParser.getRecurringDateGroup();
		if (itemRecurringDateGroup==null){
			return;
		}
		itemIsRecurring = recurringTimeParser.getRecurring();
		extractExcept(recurringTimeParser);
	}

	private void extractExcept(DateTimeParser recurringTimeParser) {
		commandArgumentsString=commandArgumentsString.replace(recurringTimeParser.getString(), STRING_EMPTY);
		DateTimeParser exceptTimeParser = new DateTimeParser(DATETIMEPARSER_INDICATOR_EXCEPT,commandArgumentsString);
		if (canGetExceptDateGroups(exceptTimeParser)){
			exceptStartDate=exceptTimeParser.getExceptStartDateGroup().getDates().get(0);
			exceptEndDate=exceptTimeParser.getExceptEndDateGroup().getDates().get(0);
		}
	}

	private boolean canGetExceptDateGroups(DateTimeParser exceptDateTimeParser) {
		return exceptDateTimeParser.getExceptEndDateGroup()!=null &&
				exceptDateTimeParser.getExceptStartDateGroup()!=null;
	}
	
		
	private String removeFieldFromArgument(int indexPrefixBegin, int indexSpace) {
		if (isValidIndex(indexSpace)){
			return commandArgumentsString.substring(INDEX_COMMAND_BEGIN, indexPrefixBegin);
		}
			return commandArgumentsString.substring(INDEX_COMMAND_BEGIN, indexPrefixBegin)
									+commandArgumentsString.substring(indexSpace);
	}
	
	private int getPrefixEndIndex(int index, String prefix) {
		return index+prefix.length();
	}
	
	private String extractFieldData(int indexOfPrefix, int indexSpace, String prefix) {
		if (isValidIndex(indexSpace)){
			return  commandArgumentsString.substring(getPrefixEndIndex(indexOfPrefix, prefix));
		}
		return commandArgumentsString.substring(getPrefixEndIndex(indexOfPrefix, prefix), indexSpace);
	}
	
	public String getTitle(){
		return itemTitle;
	}
	public String getDescription(){
		return itemDescription;
	}
	public String getPriority(){
		return itemPriority;
	}
	public String getStatus(){
		return itemStatus;
	}
	public Date getStartDate(){
		return itemStartDate;
	}
	public Date getEndDate(){
		return itemEndDate;
	}
	
	public boolean getIsRecurring(){
		return itemIsRecurring;
	}
	
	public long getRecurringPeriod(){
		return itemRecurringPeriod;
	}
	
	public boolean isNullTitle(){
		return itemTitle==null;
	}
	
	public boolean isNullDescription(){
		return itemDescription==null;
	}
	
	public boolean isNullPriority(){
		return itemPriority==null;
	}
	
	public boolean isNullStatus(){
		return itemStatus==null;
	}
	
	public boolean isNullLabel(){
		return itemLabel==null;
	}
	
	public boolean isNullStartDate(){
		return itemStartDate==null;
	}
	
	public boolean isNullEndDate(){
		return itemEndDate==null;
	}

	public boolean hasCommandTitleOnly(){
		return (isNullDescription() 
				&& isNullPriority()
				&& isNullStatus()
				&& isNullLabel()
				&& isNullStartDate()
				&& isNullEndDate());
	}
	
	public boolean hasCommandTitleAndEndDateOnly(){
		return (isNullDescription() 
				&& isNullPriority()
				&& isNullStatus()
				&& isNullLabel()
				&& isNullStartDate());
	}
	
	public boolean hasCommandTitleAndStartDateOnly(){
		return (isNullDescription() 
				&& isNullPriority()
				&& isNullStatus()
				&& isNullLabel()
				&& isNullEndDate());
	}

	private boolean isValidIndex(int index) {
		return index!=INDEX_INVALID;
	}
	

	private boolean hasNoTitleToExtract() {
		return commandArgumentsString.trim().equals(STRING_EMPTY);
	}
	
	private boolean isEmptyAddCommandList(ArrayList<AddCommand> addCommandList) {
		return addCommandList==null;
	}
	
	private boolean isSkippableDate(Date mostRecent) {
		return hasExceptDates() && isBetweenExceptDates(mostRecent);
	}
	
	private boolean isBetweenExceptDates(Date checkDate){
		return checkDate.before(exceptEndDate)
				&& checkDate.after(exceptStartDate);
	}

	private boolean hasExceptDates() {
		return exceptEndDate!=null && exceptStartDate!=null;
	}
}