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
	
	/*
	 * Possible add commands:
	 * add title = adds title without any extra stuff
	 * add title date = add title + date
	 * add title:desc date = add title + desc + date
	 * add title:desc p:high date = add title + description + priority + date
	 * add title:desc p:high l:label date = add title + desc + priority + label + date
	 * add title:desc p:high l:label s:startdate date = add title desc priority + label + startdate + enddate 
	 */
	
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
	
	private static final int INDEX_INVALID = -1;
	private static final int INDEX_COMMAND_BEGIN = 0;
	

	private final String COMMAND_PREFIX_LABEL = "l:";
	private final String COMMAND_PREFIX_STATUS = "s:";
	private final String COMMAND_PREFIX_PRIORITY = "p:";
	private final String COMMAND_PREFIX_DESCRIPTION = ":";
	private final String STRING_SPACE = " ";
	
	
	public AddParser(String userCommand){
		super(userCommand);	
		if (this.hasNoArguments){
			invalidArgs();
			return;
		} else {			
			extractPriority();
			extractStatus();
			extractLabel();
			extractRecurring();
			extractStartDate();
			extractEndDate();	
			extractDescription();	
			extractTitle();
		}
	}
	

	private void extractTitle() {
		if (commandArgumentsString.trim().equals("")){
			itemTitle=itemEndDateTitle;
			itemEndDate=new Date();
		} else{
			itemTitle = commandArgumentsString.trim();
		}
	}
	
	protected Command executeCommand(){
		if (itemIsRecurring){
			ArrayList<AddCommand> addCommandList= executeRecurring();
			if (addCommandList==null){
				return new InvalidCommand(commandArgumentsString);
			}
			return new AddRecurringCommand(addCommandList);
		} else{
			return executeNormal();
		}
	}
	
	protected Command executeNormal(){
		Date currentDate = new Date();
		if(hasCommandTitleOnly()){
			return new AddCommand(POMPOM.LABEL_TASK, itemTitle, null, null, POMPOM.STATUS_FLOATING, 
					null, null, null);
		} else if (hasCommandTitleAndEndDateOnly()){
			return new AddCommand(POMPOM.LABEL_TASK, itemTitle, null, null, POMPOM.STATUS_ONGOING, 
					null, currentDate, itemEndDate);
		} else if (hasCommandTitleAndStartDateOnly()){
			return new AddCommand(POMPOM.LABEL_TASK, itemTitle, null, null, POMPOM.STATUS_ONGOING, 
					null, itemStartDate, null);	
		} else {
			return new AddCommand(POMPOM.LABEL_EVENT, itemTitle, itemDescription, itemPriority, POMPOM.STATUS_ONGOING,
					itemLabel, itemStartDate, itemEndDate);
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
		int indexSpace = commandArgumentsString.indexOf(" ",indexOfPrefix);
		if (isValidIndex(indexOfPrefix)){
			itemPriority = extractFieldData(indexOfPrefix, indexSpace);
			commandArgumentsString = removeFieldFromArgument(indexOfPrefix, indexSpace);
		}
	}
	
	public void extractStatus(){
		int indexOfPrefix = commandArgumentsString.indexOf(COMMAND_PREFIX_STATUS); 
		int indexSpace = commandArgumentsString.indexOf(STRING_SPACE,indexOfPrefix);
		if (isValidIndex(indexOfPrefix)){
			itemStatus = extractFieldData(indexOfPrefix, indexSpace);
			commandArgumentsString = removeFieldFromArgument(indexOfPrefix, indexSpace);
		}
	}
	
	public void extractLabel(){
		int indexOfPrefix = commandArgumentsString.indexOf(COMMAND_PREFIX_LABEL);	
		int indexSpace = commandArgumentsString.indexOf(STRING_SPACE,indexOfPrefix);
		if (isValidIndex(indexOfPrefix)){
			itemLabel = extractFieldData(indexOfPrefix, indexSpace);
			commandArgumentsString = removeFieldFromArgument(indexOfPrefix, indexSpace);
		}
	}
	
	public void extractStartDate(){
		DateTimeParser dp = new DateTimeParser("start",commandArgumentsString);	
		commandArgumentsString = commandArgumentsString.replace(dp.getString(), "");
		itemStartDate=dp.getDate();
		
	}
	
	public void extractEndDate(){
		DateTimeParser dp = new DateTimeParser("end",commandArgumentsString);
		commandArgumentsString = commandArgumentsString.replace(dp.getString(), "");
		itemEndDate=dp.getDate();
		itemEndDateTitle=dp.getString();
	}
	
	public void extractRecurring(){
		DateTimeParser dp = new DateTimeParser("recurring",commandArgumentsString);
		commandArgumentsString = commandArgumentsString.replace(dp.getString(), "");
		itemRecurringDateGroup=dp.getRecurringDateGroup();
		if (itemRecurringDateGroup==null){
			return;
		}
		itemIsRecurring = dp.getRecurring();
	}
	
	private ArrayList<AddCommand> executeRecurring(){
		Date currentDate = new Date();
		ArrayList<AddCommand> output=new ArrayList<AddCommand>();
		AddCommand defaultAddCommandFormat;
		Date mostRecent = itemRecurringDateGroup.getDates().get(0);
		String recurDateString = itemRecurringDateGroup.getText();
		long newRecurInterval=DateTimeParser.calculateInterval(recurDateString);
		
		long currentTime = (new Date().getTime());
		long recurInterval=itemRecurringDateGroup.getRecurInterval();
		mostRecent= new Date(recurInterval+currentTime);
		recurInterval+=newRecurInterval;
		Date mostRecentEnd=new Date(recurInterval+currentTime);

		while (mostRecentEnd.before(itemEndDate)){
			System.out.println("mostRecent: " + mostRecent.toString());
			if (hasCommandTitleAndEndDateOnly()){
				 defaultAddCommandFormat = new AddCommand(POMPOM.LABEL_TASK, itemTitle, null, null, POMPOM.STATUS_ONGOING, 
						null, mostRecent, mostRecentEnd);
			} else if (!isNullTitle() && ! isNullEndDate()) {
				defaultAddCommandFormat = new AddCommand(POMPOM.LABEL_EVENT, itemTitle, itemDescription, itemPriority, POMPOM.STATUS_ONGOING,
						itemLabel, mostRecent, mostRecentEnd);
			} else{
				return null; //wrong format!
			}
			output.add(defaultAddCommandFormat);
			recurInterval+= newRecurInterval;
			mostRecent= mostRecentEnd;
			mostRecentEnd=new Date(recurInterval+currentTime);
		}
		return output;
	}
	
	private String removeFieldFromArgument(int indexPrefixBegin, int indexSpace) {
		if (indexSpace==-1){
			return commandArgumentsString.substring(INDEX_COMMAND_BEGIN, indexPrefixBegin);
		}
			return commandArgumentsString.substring(INDEX_COMMAND_BEGIN, indexPrefixBegin)
									+commandArgumentsString.substring(indexSpace);
	}
	
	private int getPrefixEndIndex(int index) {
		return index+2;
	}
	
	private String extractFieldData(int indexOfPrefix, int indexSpace) {
		if (indexSpace==-1){
			return  commandArgumentsString.substring(getPrefixEndIndex(indexOfPrefix));
		}
		return commandArgumentsString.substring(getPrefixEndIndex(indexOfPrefix), indexSpace);
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
	
}