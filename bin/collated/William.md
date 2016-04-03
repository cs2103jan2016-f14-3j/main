# William
###### parser\AddParser.java
``` java
 *
 */
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
		DateTimeParser dp2 = new DateTimeParser("except",commandArgumentsString);
		commandArgumentsString=commandArgumentsString.replace(dp.getString(), "");
		if (dp2.getExceptEndDateGroup()!=null && dp2.getExceptStartDateGroup()!=null){
			exceptStartDate=dp2.getExceptStartDateGroup().getDates().get(0);
			exceptEndDate=dp2.getExceptEndDateGroup().getDates().get(0);
		}
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
			if(exceptEndDate!=null && exceptStartDate!=null 
					&& mostRecent.before(exceptEndDate)
					&& mostRecent.after(exceptStartDate)){
				recurInterval+= newRecurInterval;
				mostRecent= mostRecentEnd;
				mostRecentEnd=new Date(recurInterval+currentTime);
				continue;
			} 
			
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
```
###### parser\AddParserTest.java
``` java
 *
 */
public class AddParserTest{

	Parser parser = Parser.getInstance();
	PrettyTimeParser timeParser = new PrettyTimeParser();
	
	/*
	 * Tests if can add floating tasks
	 */
	@Test
	public void testAddCommand1(){
		AddParser add = new AddParser("do project");
		assertEquals("do project",add.getTitle());
	}
	/*
	 * Tests if can add tasks with end date
	 */
	@Test
	public void testAddCommand2(){
		AddParser add = new AddParser("do project 28 march");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * Tests if can add tasks with start and end date.
	 */
	@Test
	public void testAddCommand3(){
		AddParser add = new AddParser("do project e:28 march f:16 march");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		Date startDate= timeParser.parseSyntax("16 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
		assertEquals(startDate.compareTo(add.getStartDate()),1);	
	}
	
	/*
	 * Tests if can switch the order of the title and end date
	 */
	@Test
	public void testAddCommand4(){
		AddParser add = new AddParser("28 march do project");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * Tests if can switch the order of the start date, end date and title. 
	 */
	@Test
	public void testAddCommand5(){
		AddParser add = new AddParser("e:28 march f:16 march do project");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * Tests if can switch the order of the start date, end date and title.
	 */
	@Test
	public void testAddCommand6(){
		AddParser add = new AddParser("f:16 march e:28 march do project");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * Tests if can add in recurring tasks with end date
	 */
	
	@Test
	public void testAddCommand7(){
		AddParser add = new AddParser("do cs2103 every friday e:28 march");
		assertEquals("do cs2103",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * tests if can switch the order of the fields for adding recurring tasks with
	 * end date.
	 */
	@Test
	public void testAddCommand8(){
		AddParser add = new AddParser("do cs2103 e:28 march every friday");
		assertEquals("do cs2103",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
		assertTrue(add.getIsRecurring());
	}	
	
	/*
	 * Tests if can add in a task with only a parsable title 
	 */
	@Test
	public void testAddCommand9(){
		AddParser add = new AddParser("2103");
		assertEquals("2103",add.getTitle());
		Date endDate= new Date();
		assertEquals(endDate.compareTo(add.getEndDate()),0);
	}	
}
	
	
```
###### parser\ArgsParser.java
``` java
 *
 */
public class ArgsParser {
	
	protected static Logger logger = Logger.getLogger("Parser");
	
	protected boolean hasNoArguments=false;
	protected String commandArgumentsString;
	
	public ArgsParser(String commandArguments){
		commandArgumentsString = commandArguments;
		checkForAnyArguments();
	}

	private void checkForAnyArguments() {
		if (commandArgumentsString.equals("")){	
			hasNoArguments=true;
		}
	}
	
	public Command invalidArgs(){
		return new InvalidCommand(commandArgumentsString);
	}
}
```
###### parser\DateTimeParser.java
``` java
 *
 */

public class DateTimeParser{
	
	private String originalString="";
	private String dateTimeString="";
	private String exceptString="";
	private Date dateTime=null;
	private static PrettyTimeParser timeParser=new PrettyTimeParser();
	private boolean isRecurring = false;
	private DateGroup exceptStartDateGroup=null;
	private DateGroup exceptEndDateGroup=null;
	
	private boolean isFlippedDate=false;
	
	private static final String COMMAND_PREFIX_STARTDATE = "f:";
	private static final String COMMAND_PREFIX_ENDDATE = "e:";
	private static final String COMMAND_PREFIX_RECURRING = "every";
	private static final String COMMAND_PREFIX_EXCEPT = "except";

	private static final String INDICATOR_START = "start";
	private static final String INDICATOR_END = "end";
	private static final String INDICATOR_RECURRING = "recurring";	
	private static final String INDICATOR_EXCEPT = "except";
	
	private static DateGroup recurringDateGroup=null;
	
	private static Logger logger = Logger.getLogger("Parser");
	public DateTimeParser(String parseType, String commandArgumentsString){
		
		if(parseType.equals(INDICATOR_RECURRING)){
			commandArgumentsString=extractRecurringString(commandArgumentsString);
			commandArgumentsString=extractExceptDates(commandArgumentsString);
		} else if (parseType.equals(INDICATOR_EXCEPT)){
			commandArgumentsString=extractExceptDates(commandArgumentsString);
		} else{
			commandArgumentsString=breakUpStartAndEndDates(parseType, commandArgumentsString);			
		}
		originalString=correctDateFormat(commandArgumentsString);
		if(parseType.equals(INDICATOR_START)){
				processStartDate();
		} else if (parseType.equals(INDICATOR_END)){
				processEndDate();	
		} else if (parseType.equals(INDICATOR_RECURRING)){
				processRecurring();	
		}else if (parseType.equals(INDICATOR_EXCEPT)){
				processExcept();
		} else{
			logger.log(Level.SEVERE,"programming error here. never specify date is end date or start date processing");
			return;
		}
		if (isFlippedDate){
			dateTimeString=reverseCorrectDateFormat(dateTimeString);
		}
		
	}
	
	static String breakUpStartAndEndDates(String startOrEnd, String rawString){
		String firstPortion ="";
		String restOfTheString="";
		int startIndex = rawString.indexOf(COMMAND_PREFIX_STARTDATE);
		int endIndex = rawString.indexOf(COMMAND_PREFIX_ENDDATE);
		int minIndex=Math.min(startIndex, endIndex);
		int maxIndex=Math.max(startIndex, endIndex);
		
		if (startIndex<0 || endIndex<0){
			return rawString;
		} else{
			firstPortion= rawString.substring(minIndex,maxIndex);
			restOfTheString=rawString.replace(firstPortion,"");
		}	
		if (startOrEnd.equals("start")){
			if (firstPortion.indexOf(COMMAND_PREFIX_STARTDATE)>=0){
				return firstPortion.trim();
			} else{
				return restOfTheString;
			}
		}else if (startOrEnd.equals("end")){
			if (firstPortion.indexOf(COMMAND_PREFIX_ENDDATE)>=0){
				return firstPortion.trim();
			} else{
				return restOfTheString;
			}
		} else{
			logger.log(Level.SEVERE,"invalid startOrEnd given for breakupStart()");
			return rawString;
		}
	}
	
	static String extractRecurringString(String rawString){
		String firstPortion ="";
		String restOfTheString="";
		int endIndex = rawString.indexOf(COMMAND_PREFIX_ENDDATE);
		int recurringIndex = rawString.indexOf(COMMAND_PREFIX_RECURRING);
		int minIndex=Math.min(endIndex, recurringIndex);
		int maxIndex=Math.max(endIndex, recurringIndex);
		
		if (endIndex<0 || recurringIndex<0){
			return rawString;
		} else{
			firstPortion= rawString.substring(minIndex,maxIndex);
			restOfTheString=rawString.replace(firstPortion,"");
		}
		if (firstPortion.contains(COMMAND_PREFIX_RECURRING)){
			return firstPortion;
		} else{
			return restOfTheString;
		}
	}
	
	static String extractExceptDates(String rawString){
		String firstPortion ="";
		String restOfTheString="";
		int endIndex = rawString.indexOf(COMMAND_PREFIX_ENDDATE);
		int exceptIndex = rawString.indexOf(COMMAND_PREFIX_EXCEPT);
		int minIndex=Math.min(endIndex, exceptIndex);
		int maxIndex=Math.max(endIndex, exceptIndex);
		
		if (endIndex<0 || exceptIndex<0){
			return rawString;
		} else{
			firstPortion= rawString.substring(minIndex,maxIndex);
			restOfTheString=rawString.replace(firstPortion,"");
		}
		if (firstPortion.contains(COMMAND_PREFIX_EXCEPT)){
			return firstPortion;
		} else{
			return restOfTheString;
		}
	}
	
	private void processStartDate(){
		//unique feature of start date: f:
		if (!hasStartPrefix(originalString)){
			return;
		}
		originalString = originalString.replace(COMMAND_PREFIX_STARTDATE,"");;
		List<DateGroup> dateGroup=parseAndCheckDate(originalString);
		dateTimeString="f:"+dateTimeString;
		dateTime=dateGroup.get(0).getDates().get(0);
	}
	
	private void processEndDate(){
		List<DateGroup> dateGroup;
		//unique feature of end date: after title:description is the enddate.
		if (hasEndPrefix(originalString)){
			originalString = originalString.replace(COMMAND_PREFIX_ENDDATE,"");
			dateGroup=parseAndCheckDate(originalString);
			dateTimeString="e:"+dateTimeString;
		} else{
			dateGroup=parseAndCheckDate(originalString);
		}
		if (!isEmptyDateGroup(dateGroup)){
			dateTime=dateGroup.get(0).getDates().get(0);
		}
	}
	
	private void processRecurring(){
		
		if (!originalString.contains(COMMAND_PREFIX_RECURRING)){
			return;
		} 
		List<DateGroup> dateGroup=parseAndCheckDate(originalString);
		recurringDateGroup=dateGroup.get(0);
	}
	
	private void processExcept(){
		
		if (!originalString.contains(COMMAND_PREFIX_EXCEPT)){
			return;
		} 
		String[] datesSplit = originalString.split("to");
		
		if (datesSplit.length!=2){
			return;
		}
		exceptStartDateGroup=parseAndCheckDate(datesSplit[0]).get(0);
		exceptEndDateGroup=parseAndCheckDate(datesSplit[1]).get(0);
	}
	
	public boolean isEmptyDateGroup(List<DateGroup> dateGroup){
		return dateGroup.size()<=0;
	}
	
	public boolean hasParsableDate(String unparsedString){
		return !isEmptyDateGroup(timeParser.parseSyntax(unparsedString));
	}
	
	public List<DateGroup> parseAndCheckDate(String stringWithDate){
		String output="";
		String initialParsedString = parseDateToString(stringWithDate);
		if (initialParsedString.equals("")){
			return timeParser.parseSyntax("");
		}
		String[] rawStringArray = stringWithDate.split(" ");
		String[] parsedStringArray = initialParsedString.split(" ");
		if (Arrays.equals(rawStringArray, parsedStringArray)){
			dateTimeString=stringWithDate;
			return timeParser.parseSyntax(stringWithDate);
		} else{
			for (int i=0; i<rawStringArray.length; i++){
				for (int j=0; j<parsedStringArray.length; j++){
					
					String currentRawStringPortion = rawStringArray[i];
					String currentParsedStringPortion = parsedStringArray[j];
				
					if (currentRawStringPortion.equals(currentParsedStringPortion) && !output.contains(currentRawStringPortion)){
						output+=(rawStringArray[i] + " ");
					} else if (!currentRawStringPortion.equals(currentParsedStringPortion) 
							&& currentRawStringPortion.contains(currentParsedStringPortion)){
						output = foo(stringWithDate, currentRawStringPortion);
						return parseAndCheckDate(output.trim());
					 	
						} else{ 
							//do nothing
					}
				} 
			}
		}
		return parseAndCheckDate(output.trim());
	}

	//detect messed up titles
	private String foo(String stringWithDate, String currentRawStringPortion) {
		int index = stringWithDate.indexOf(currentRawStringPortion);
		int indexSpace = stringWithDate.indexOf(" ",index);
		if (indexSpace==-1){
			stringWithDate="";
		} else{
			stringWithDate=stringWithDate.substring(0,index)+
					stringWithDate.substring(indexSpace+1);	
		}
		return stringWithDate;
	}
	
	private String parseDateToString(String dateString){
		String output="";
		try{
			if (timeParser.parseSyntax(dateString).get(0).isRecurring()){
				output+="every ";
				isRecurring=true;
			}	
			output+=timeParser.parseSyntax(dateString).get(0).getText();
		} catch (Exception e){
			output= "";
		}
			return output;
	}
	
	private String correctDateFormat(String commandArguments){
		String[] commandArgumentsArray = commandArguments.split(" ");
		DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
		formatter1.setLenient(false);
		String prefix = null;
		for (String stringPortion:commandArgumentsArray){
			try {
				if (hasPrefix(stringPortion)){
					prefix = stringPortion.substring(0, 2);
					stringPortion=stringPortion.substring(2);				
				}
				formatter1.parse(stringPortion);	
				isFlippedDate=true;
				String[] dateArray = stringPortion.split("/");
				String newDateString = dateArray[1]+"/"+dateArray[0]+"/"+dateArray[2];
				if (prefix!=null){
					stringPortion=prefix+stringPortion;
					newDateString=prefix+newDateString;
				}
				commandArguments = commandArguments.replace(stringPortion,newDateString);
				return commandArguments;
			} catch (ParseException e) {
				//If input date is in different format or invalid.
			}
		}
		return commandArguments;
	}
	
	private String reverseCorrectDateFormat(String commandArguments){
		String[] commandArgumentsArray = commandArguments.split(" ");
		DateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
		formatter1.setLenient(false);
		isFlippedDate=false;
		String prefix = null;
		for (String stringPortion:commandArgumentsArray){
			try{
				if (hasPrefix(stringPortion)){
					prefix = stringPortion.substring(0, 2);
					stringPortion=stringPortion.substring(2);				
				}	
				formatter1.parse(stringPortion);	
				String[] dateArray = stringPortion.split("/");
				String newDateString = dateArray[1]+"/"+dateArray[0]+"/"+dateArray[2];
				if (prefix!=null){
					stringPortion=prefix+stringPortion;
					newDateString=prefix+newDateString;
				}
				commandArguments = commandArguments.replace(stringPortion,newDateString);
				return commandArguments;
			}catch (ParseException e){
				//do nothing
			}
		}
		return commandArguments;
	}
	
	public static boolean hasPrefix(String toCheck){
		if (isValidSubstring(toCheck,COMMAND_PREFIX_STARTDATE)
				|| isValidSubstring(toCheck,COMMAND_PREFIX_ENDDATE)){
			return true;
		} else{
			return false;
		}
	}
	
	public static boolean hasStartPrefix(String toCheck){
		if (isValidSubstring(toCheck,COMMAND_PREFIX_STARTDATE)){
			return true;
		} else{
			return false;
		}
	}
	
	public static boolean hasEndPrefix(String toCheck){
		if (isValidSubstring(toCheck,COMMAND_PREFIX_ENDDATE)){
			return true;
		} else{
			return false;
		}
	}
	
	public static long calculateInterval(String day){
		Date dateone=timeParser.parseSyntax("last " + day).get(0).getDates().get(0);
		Date datetwo=timeParser.parseSyntax("next " +day).get(0).getDates().get(0);
		long interval = datetwo.getTime()-dateone.getTime();
		long output = 1000*60*60*24*7;
		return (interval)/10*10/2;
	}

	private static boolean isValidSubstring(String toCheck, String substring) {
		return toCheck.contains(substring);
	}
	
	
	public Date getDate(){
		return dateTime;
	}
	
	public DateGroup getRecurringDateGroup(){
		return recurringDateGroup;
	}
	
	public String getString(){
		return dateTimeString;
	}
	
	public boolean getRecurring(){
		return isRecurring;
	}
	
	public DateGroup getExceptStartDateGroup(){
		return exceptStartDateGroup;
	}
	
	public DateGroup getExceptEndDateGroup(){
		return exceptEndDateGroup;
	}
	

}
	
```
###### parser\DateTimeParserTest.java
``` java
 *
 */

public class DateTimeParserTest {

	PrettyTimeParser timeParser = new PrettyTimeParser();
	
	
	
	@Test
	public void testEveryWeek() {
		DateTimeParser dp = new DateTimeParser("end","shopping every monday");
		String output = dp.getString();
		assertEquals(output,"every monday");
	}
	
	@Test
	public void testFromEveryWeek() {
		//make this invalid!
		DateTimeParser dp = new DateTimeParser("start","shopping f:every monday");
		String output = dp.getString();
		assertEquals(output,"f:every monday");
	}
	
	@Test
	public void testFormat1() {
		DateTimeParser dp = new DateTimeParser("end","shopping e:28 march f:16 march");
		String output = dp.getString();
		assertEquals(output,"e:28 march");
	}
	
	@Test
	public void testFormat2() {
		DateTimeParser dp = new DateTimeParser("start","shopping f:16 march e:28 march");
		String output = dp.getString();
		assertEquals(output,"f:16 march");
	}
	
	@Test
	public void testFormat3() {
		DateTimeParser dp = new DateTimeParser("end","shopping 16/03/2016");
		String output = dp.getString();
		assertEquals(output,"16/03/2016");
	}
	
	@Test
	public void testFormat4() {
		DateTimeParser dp = new DateTimeParser("start","shopping f:16/03/2016 e:28/03/2016");
		String output = dp.getString();
		assertEquals(output,"f:16/03/2016");
	}
	
	@Test
	public void testFormat5() {
		DateTimeParser dp = new DateTimeParser("start","shopping f:16 march e:28/03/2016");
		String output = dp.getString();
		assertEquals(output,"f:16 march");
	}
	
	@Test
	public void testFormat6() {
		DateTimeParser dp = new DateTimeParser("end","shopping e:28/03/2016 f:16 march");
		String output = dp.getString();
		assertEquals(output,"e:28/03/2016");
	}
	
	@Test
	public void testFormat7() {
		DateTimeParser dp = new DateTimeParser("end","shopping e:28/03/2016 f:16 mar");
		String output = dp.getString();
		assertEquals(output,"e:28/03/2016");
	}
	
	@Test
	public void testFormat8() {
		DateTimeParser dp = new DateTimeParser("end","shopping 16 march");
		String output = dp.getString();
		assertEquals(output,"16 march");
	}
	
	@Test
	public void testHasPrefix() {
		boolean output = DateTimeParser.hasPrefix("shopping 16 march");
		assertFalse(output);
	}
	
	@Test
	public void testAddStart() {
		DateTimeParser dp = new DateTimeParser("end","shopping tomorrow");
		Date endDate = timeParser.parseSyntax("tomorrow").get(0).getDates().get(0);
		Date output = dp.getDate();
		assertEquals(endDate.toString().trim(),output.toString().trim());
	}
	
	@Test
	public void testbreakUpStartAndEndDatesStart(){
		String output=DateTimeParser.breakUpStartAndEndDates("start","do project e:28 march f:16 march");
		assertEquals(output,"do project f:16 march");
	}
	
	@Test
	public void testbreakUpStartAndEndDatesEnd(){
		String output=DateTimeParser.breakUpStartAndEndDates("end","do project e:28 march f:16 march");
		assertEquals(output,"e:28 march");
	}
	
//	@Test
	public void testParseMessedUpTitle() {
		DateTimeParser dp = new DateTimeParser("end","do cs1231231231231232132341 tomorrow");
		Date endDate = timeParser.parseSyntax("tomorrow").get(0).getDates().get(0);
		Date output = dp.getDate();
		assertEquals(endDate.toString().trim(),output.toString().trim());
	}
	
	@Test
	public void testPrettyTimeParser(){
		PrettyTimeParser parser = new PrettyTimeParser();
		List<DateGroup> dg=parser.parseSyntax("every monday until 28 march");
		Date d= dg.get(0).getDates().get(0);
		System.out.println(d.toString());
		System.out.println(dg.get(0).getRecursUntil());
		System.out.println(dg.get(0).getText());
	}
	
	@Test
	public void testCalculateInterval(){
		long testOutput = DateTimeParser.calculateInterval("friday");
		assertEquals(testOutput,1000*60*60*24*7);	
	}
	
	//confirms that the recurring option for DateTimeParser gives back the upcoming day specified
	@Test
	public void testEveryRecurring(){
		DateTimeParser dp = new DateTimeParser("recurring","do cs2103 e:28 march 0000 every wednesday");
		Date testDate = dp.getRecurringDateGroup().getDates().get(0);
		Date expectedDate = new PrettyTimeParser().parseSyntax("wednesday").get(0).getDates().get(0);
		assertEquals(testDate.toString(),expectedDate.toString());
	}
	
	@Test
	public void testEveryEnd(){
		DateTimeParser dp = new DateTimeParser("end","do cs2103 e:28 march every friday");
		System.out.println(dp.getString());
		System.out.println(dp.getDate().toString());
	}
	
	@Test
	public void testParser(){
		PrettyTimeParser parser = new PrettyTimeParser();
		List<DateGroup> dg=parser.parseSyntax("every monday until 28 march except 1 march to 16 march");	
	}
	
	@Test
	public void testAddNumberOnly() {
		DateTimeParser dp = new DateTimeParser("end","2103");
		System.out.println(dp.getString());
		System.out.println(dp.getDate().toString());
	}
	
	@Test
	public void testExtractException(){
		DateTimeParser dp = new DateTimeParser("except","e: 28 march except 16 march to 31 march");
		//System.out.println(dp.getString());
		
	}
	
	

}
```
###### parser\DeleteParser.java
``` java
 *
 */

public class DeleteParser extends ArgsParser{
	
	private int itemID;
	private Command outputCommand = null;
	
	public DeleteParser(String userCommand){
		super(userCommand);
		getItemId();
	}
	
	public Command executeCommand(){
		if (outputCommand == null){
			System.out.println(commandArgumentsString + "lol");
			return new DelCommand(itemID);
		} else{
			return outputCommand;
		}
		
	}
	
	public void getItemId(){
		try{
			itemID = Integer.parseInt(commandArgumentsString);
		} catch (Exception e){
			InvalidParser InvalidArgumentParser = new InvalidParser(commandArgumentsString);
			outputCommand = InvalidArgumentParser.executeCommand();
			logger.log(Level.INFO,"tried to parse '"+commandArgumentsString+"' into integer but failed.");
		}
	}	
}
```
###### parser\EditParser.java
``` java
 *
 */


public class EditParser extends ArgsParser{
	
	private final int INDEX_TASK_ID = 0;
	private final int INDEX_FIELDS = 1;
	private final int INDEX_NEW_DATAS = 2; 
	
	private int taskID;
	private String field;
	private String newData;
	
	private ArrayList<String> argsArray;
	
	
	public EditParser(String userCommand){
		super(userCommand);
		
		argsArray = new ArrayList<String>(Arrays.asList(commandArgumentsString.split(" ")));
		extractTaskID();
		System.out.println(argsArray.toString());
		extractFields();
		System.out.println(argsArray.toString());
		extractNewData();
	}
	
	public Command executeCommand(){
		return new EditCommand(taskID, field, newData); 
	}
	
	public void extractTaskID(){
		try{
			taskID = Integer.parseInt(argsArray.remove(0));
		} catch (Exception e){
			taskID = -1;
		}
	}
	
	public void extractFields(){
		field = argsArray.remove(0);
	}
	
	public void extractNewData(){
		newData = String.join(" ",argsArray);
	}
	
	public String getField(){
		return field;
	}
	
	public String getNewData(){
		return newData;
	}
	
}
```
###### parser\EditParserTest.java
``` java
 *
 */


public class EditParserTest {

	@Test
	public void testGetFields() {
		EditParser editParser = new EditParser("1 title new title");
		assertEquals(editParser.getField(),"title");
	}	
	/*
	 * New data field has the name of the title inside.
	 */
	@Test
	public void testGetNewData() {
		EditParser editParser = new EditParser("1 title new title");
		
		assertEquals(editParser.getNewData(),"new title");
	}

}
```
###### parser\ExitParser.java
``` java
 *
 */
public class ExitParser {
	public void executeCommand(){
		new ExitCommand();
		
	}
}
```
###### parser\InvalidParser.java
``` java
 *
 */


public class InvalidParser {
	
	private String invalidCommand;
	public InvalidParser(String userCommand){
		invalidCommand = userCommand;
	}
	
	public Command executeCommand(){
		return new InvalidCommand(invalidCommand);
	}
}
```
###### parser\Parser.java
``` java
 *
 */

public class Parser{
	
	/** List of Command types */
	private static final String CMD_ADD = "add";
	private static final String CMD_DELETE = "delete";
	private static final String CMD_DONE = "done";
	private static final String CMD_EDIT = "edit";
	private static final String CMD_EXIT = "exit";
	private static final String CMD_SEARCH = "search";
	private static final String CMD_SHOW = "show";
	private static final String CMD_UNDO ="undo";
	private static final String CMD_PATH = "setpath";
	
	private static final int COMMAND_ARRAY_SIZE = 2; 
	private static final int COMMAND_TYPE_INDEX = 0;
	private static final int COMMAND_ARGUMENT_INDEX = 1;
	
	private static Parser parserInstance;
		
	private static Logger logger = Logger.getLogger("Parser");
	
	public static Parser getInstance()
	{
		if (parserInstance == null)
			parserInstance = new Parser();

		return parserInstance;
	}
	
	public Parser(){

	}
	
	/**
	 * This operation takes in the command specified by the
	 * user, executes it and returns a message about
	 * the execution information to the user.
	 * 
	 * @param userCommand
	 * 			is the command the user has given to the
	 * 			program
	 * @return
	 * 		the message containing information about the
	 * 		execution of the command. 
	 */
	public Command executeCommand(String userCommand){	
		
		
		String[] parsedCommandArray = splitCommand(userCommand);
		String commandType = parsedCommandArray[COMMAND_TYPE_INDEX];
		String commandArgument = parsedCommandArray[COMMAND_ARGUMENT_INDEX];

		
		switch (commandType){
			case CMD_ADD:
				AddParser addArgumentParser = new AddParser(commandArgument);
				return addArgumentParser.executeCommand();
			case CMD_DELETE:
				System.out.println("delete test");
				DeleteParser deleteArgumentParser = new DeleteParser(commandArgument);
				return deleteArgumentParser.executeCommand();
			case CMD_EDIT:
				EditParser EditArgumentParser = new EditParser(commandArgument);
				return EditArgumentParser.executeCommand();
			case CMD_SEARCH:
				SearchParser searchParser = new SearchParser(commandArgument);
				return searchParser.executeCommand();
			case CMD_SHOW:
				//return new ShowParser(commandArgument);
				//Hard coded
			case CMD_EXIT:
				ExitParser exitParser = new ExitParser();
				//return exitParser.executeCommand();
				System.exit(0);
			case CMD_UNDO:
				UndoParser undoparser = new UndoParser();
				return undoparser.executeCommand();
			case CMD_PATH:
				return new PathCommand(commandArgument);
	}
		InvalidParser InvalidArgumentParser = new InvalidParser(userCommand);
		return InvalidArgumentParser.executeCommand();

	}
	
	private String[] splitCommand(String userCommand){
		String[] outputArray = new String[COMMAND_ARRAY_SIZE];
		outputArray[COMMAND_TYPE_INDEX] = getCommandType(userCommand);
		outputArray[COMMAND_ARGUMENT_INDEX] = getArguments(userCommand, outputArray[0]);
		return outputArray;
	}
	
	private String getCommandType(String userCommand){
		String[] toSplit = userCommand.split(" ", COMMAND_ARRAY_SIZE);
		return toSplit[COMMAND_TYPE_INDEX].toLowerCase().trim();
	}
	
	private String getArguments(String userCommand, String commandType) {
		String[] toSplit = userCommand.split(" ", COMMAND_ARRAY_SIZE);
		if (toSplit.length==2){
			return toSplit[COMMAND_ARGUMENT_INDEX].toLowerCase().trim();
		}
		return "";
	}

}
 	
```
###### parser\ParserTest.java
``` java
 *
 */


public class ParserTest {
	POMPOM pompom = new POMPOM();
	Parser parser = Parser.getInstance();
	
	/*
	 * This is the boundary case for the valid user commands partition
	 */
	@Test
	public void testAddCommand() {
		Command outputCommand = parser.executeCommand("add do cs2013");
		assertTrue(outputCommand instanceof command.AddCommand);
	}
	
	/*
	 * This is the boundary case for the invalid user commands partition
	 */
	@Test
	public void testFailCommand() {
		Command outputCommand = parser.executeCommand("ad do cs2013");
		assertTrue(outputCommand instanceof command.InvalidCommand);
	}


}
```
###### parser\parseTest.java
``` java
 *
 */

public class parseTest{

	Parser parser = new Parser();
	

	//@Test
	public static void testAddParser() {
		//edit 1 status asd
		//add go club
		//add do homework next week
		//add do cs2103:finish v0.1 p:high l:hw s:open f:next monday next tuesday
		//edit 1 title asd
		//delete 1
		//exit
	}
	
	public static void prettyTime(){
		PrettyTimeParser timeParser = new PrettyTimeParser();
		List<DateGroup> dgl = timeParser.parseSyntax("every day");
		for (DateGroup dg: dgl){
			System.out.println(dg.getText());
		}	
		System.out.println("ended");
	}
	public static void main(String[] args){
		prettyTime();
	}
}
	
	
```
###### parser\SearchParser.java
``` java
 *
 */

public class SearchParser extends ArgsParser{
	String keyWord;
	public SearchParser(String commandArguments) {
		super(commandArguments);
		keyWord = commandArguments;
	}
	public Command executeCommand(){
		
			System.out.println(commandArgumentsString + "lol");
			return new SearchCommand(keyWord);
		
		
	}

}
```
###### parser\UndoParser.java
``` java
 *
 */

public class UndoParser {
	
	public Command executeCommand(){
		return new UndoCommand();
		
	}
	
}
```
