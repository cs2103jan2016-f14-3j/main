package parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

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
		System.out.println(Arrays.toString(rawStringArray)+" "+(Arrays.toString(parsedStringArray)));
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
						//System.out.println("trys: lol");
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
		System.out.println(dateone.toString());
		System.out.println(datetwo.toString());
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
	