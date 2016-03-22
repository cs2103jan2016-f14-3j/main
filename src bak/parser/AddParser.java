package parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;
import static org.junit.Assert.assertNotNull;

import command.Command;
import command.InvalidCommand;
import main.POMPOM;
import storage.Storage;
import command.AddCommand;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;


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
	
	private String itemTitle= "";
	private String itemDescription = "";
	private String itemPriority = "";
	private String itemStatus = "";
	private String itemLabel = "";
	private Date itemStartDate = null;
	private Date itemEndDate = new Date();
	
	private PrettyTimeParser timeParser = Storage.timeParser;
	
	private static final int INDEX_INVALID = -1;
	private static final int INDEX_PREFIX_LENGTH = 2;
	private static final int INDEX_COMMAND_BEGIN = 0;
	
	private final String COMMAND_PREFIX_STARTDATE = "f:";
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
			assertNotNull(timeParser);
			extractPriority();
			extractStatus();
			extractLabel();
			extractStartDate();
			extractEndDate();
			extractDescription();
			extractTitle();
		}
	}
	

	private void extractTitle() {
		itemTitle = commandArgumentsString.trim();
	}
	
	protected Command executeCommand(){
		fieldNotNullChecker();
		if (isAddCommandType1()){
			return new AddCommand(itemTitle);
		} else if (isAddCommandType2()){
			return new AddCommand(itemTitle, itemEndDate);
		} else if (isAddCommandType3()){
			return new AddCommand(itemTitle, itemDescription, itemPriority, itemStatus, 
									itemLabel, itemStartDate, itemEndDate);
		} else {
			return invalidArgs();
		}
	}

	private void fieldNotNullChecker() {
		assertNotNull(itemTitle);
		assertNotNull(itemDescription);
		assertNotNull(itemPriority);
		assertNotNull(itemLabel);
		assertNotNull(itemStatus);
		assertNotNull(itemStartDate);
		assertNotNull(itemEndDate);
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
		int indexOfPrefix = commandArgumentsString.indexOf(COMMAND_PREFIX_STARTDATE);		
		if (isValidIndex(indexOfPrefix)){
			String itemStartDateString = commandArgumentsString.substring(indexOfPrefix); 
			System.out.println("itemStartDateString: " + itemStartDateString);
			itemStartDateString = correctTimeFormat(removePrefix(itemStartDateString));
			int indexSpace = itemStartDateString.indexOf(" ");
			if (indexSpace>0){
				itemStartDateString=itemStartDateString.substring(0, indexSpace);
			}
			List<DateGroup> startDateGroups = parseAndCheckDate(itemStartDateString);
			String startDateString = startDateGroups.get(0).getText();
			System.out.println("startDateString: "+startDateString);
			DateGroup parsedDateGroup = timeParser.parseSyntax(startDateString).get(0);
			itemStartDate = parsedDateGroup.getDates().get(0);
			commandArgumentsString = commandArgumentsString.replace(COMMAND_PREFIX_STARTDATE+startDateString, "");
		} else{
			itemStartDate = new Date();
		}
	}
	
	public void extractEndDate(){ 
		commandArgumentsString = correctTimeFormat(commandArgumentsString);		
		List<DateGroup> endDateGroup = parseAndCheckDate(commandArgumentsString);
		if (endDateGroup.size()>0){
			itemEndDate=endDateGroup.get(0).getDates().get(0);
			String endDateString = endDateGroup.get(0).getText();
			if(endDateGroup.get(0).isRecurring()){
				endDateString="every "+endDateString;
			}
			System.out.println("endDateString: "+endDateString);
			commandArgumentsString = commandArgumentsString.replace(endDateString, STRING_SPACE);
			System.out.println("cmdargstr: "+commandArgumentsString);
		} else{
			itemEndDate = new Date();
		}
	}
	
	//PrettyTime is a bit stupid. need to make it smarter.
	public List<DateGroup> parseAndCheckDate(String stringWithDate){
		System.out.println("string with date: "+stringWithDate);
		String output="";
		String initialParsedString = parseDateToString(stringWithDate);
		if (initialParsedString.equals("")){
			return timeParser.parseSyntax("");
		}
		String[] rawStringArray = stringWithDate.split(" ");
		String[] parsedStringArray = initialParsedString.split(" ");

		System.out.println(Arrays.toString(rawStringArray)+" "+ Arrays.toString(parsedStringArray));
		if (Arrays.equals(rawStringArray, parsedStringArray)){
			System.out.println("stringwithedate: "+stringWithDate);
			return timeParser.parseSyntax(stringWithDate);
		} else{
			for (int i=0; i<rawStringArray.length; i++){
				for (int j=0; j<parsedStringArray.length; j++){
					String currentRawStringPortion = rawStringArray[i];
					String currentParsedStringPortion = parsedStringArray[j];
					if (currentRawStringPortion.equals(currentParsedStringPortion) && !output.contains(currentRawStringPortion)){
						output+=(rawStringArray[i] + " ");
					} else if (currentRawStringPortion.contains(currentParsedStringPortion)){
						int index = stringWithDate.indexOf(currentRawStringPortion);
						int indexSpace = stringWithDate.indexOf(" ",index);
						if (indexSpace==-1){
							stringWithDate="";
						} else{
							stringWithDate=stringWithDate.substring(0,index)+
									stringWithDate.substring(indexSpace+1);	
						}
						return parseAndCheckDate(stringWithDate);
					} else{ 
						//System.out.println("trys: lol");
					}
				} 
			}
		}
		System.out.println("output: " + output);
		return parseAndCheckDate(output.trim());
	}
	
	public String parseDateToString(String dateString){
		String output="";
		try{
			if (timeParser.parseSyntax(dateString).get(0).isRecurring()){
				output+="every ";
			}
			output+=timeParser.parseSyntax(dateString).get(0).getText();
		} catch (Exception e){
			output= "";
		}
			return output;
	}
	
	public String correctTimeFormat(String commandArguments){
		String[] commandArgumentsArray = commandArguments.split(" ");
		DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
		formatter1.setLenient(false);
		for (String stringPortion:commandArgumentsArray){
			try {
				System.out.println(stringPortion);
				Date date= formatter1.parse(stringPortion);
				String[] dateArray = stringPortion.split("/");
				String newDateString = dateArray[1]+"/"+dateArray[0]+"/"+dateArray[2];
				commandArguments = commandArguments.replace(stringPortion,newDateString);
				commandArgumentsString = commandArgumentsString.replace(stringPortion,newDateString);
				return commandArguments;
			} catch (ParseException e) {
				//If input date is in different format or invalid.
				logger.log(Level.INFO,"Tried to parse a time format other than dd/MM/yyyy ");
			}
		}
		return commandArguments;
	}
	
	private boolean isAddCommandType3(){
		
		//Add Command Type 3: All fields are filled
		//e.g. add do cs2103:do parser p:high s:open l:homework f:27 march 28 march
		
		return hasAllFieldsFilled();
	}

	private boolean isAddCommandType2(){
		
		//Add Command Type 2: only has title and end date	
		//e.g. add do cs2103 28 march

		return hasNoItemDescription() && hasNoItemPriority() && hasNoItemStatus() 
				&& hasNoItemLabel();
	}

	private boolean isAddCommandType1(){

		//Add Command Type 1: Only has title
		//e.g. add do cs2103
		return hasNoItemDescription() && hasNoItemPriority() && hasNoItemStatus() 
				&& hasNoItemLabel() && hasNoStartDate() && hasNoEndDate();
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
	
	private String removePrefix(String itemLabel){
		return itemLabel.substring(2);
	}
	
	private String extractFieldData(int indexOfPrefix, int indexSpace) {
		if (indexSpace==-1){
			return  commandArgumentsString.substring(getPrefixEndIndex(indexOfPrefix));
		}
		return commandArgumentsString.substring(getPrefixEndIndex(indexOfPrefix), indexSpace);
	}

	private boolean hasNoEndDate() {
		return itemEndDate==null;
	}

	private boolean hasNoStartDate() {
		return itemStartDate==null;
	}

	private boolean hasNoItemLabel() {
		return itemLabel==null;
	}

	private boolean hasNoItemStatus() {
		return itemStatus==null;
	}
	
	private boolean hasNoItemDescription(){
		return itemDescription==null;
	}
	
	private boolean hasNoItemPriority(){
		return itemPriority==null;
	}
	
	private boolean hasNoTitle(){
		return itemTitle==null;
	}
	
	private boolean hasAllFieldsFilled(){
		return !hasNoTitle() && !hasNoItemDescription() && !hasNoItemLabel() 
				&& !hasNoItemStatus() && !hasNoItemPriority()
				&& !hasNoStartDate() && !hasNoEndDate();
	}
	
	private boolean isValidIndex(int index) {
		return index!=INDEX_INVALID;
	}
	
}