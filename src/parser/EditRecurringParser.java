package parser;

import java.util.ArrayList;
import java.util.Date;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import command.Command;
import command.EditCommand;
import command.EditRecurringCommand;
import command.InvalidCommand;
import main.POMPOM;
/**
 *  @@author A0121760R
 *
 */
public class EditRecurringParser extends ArgsParser{
	
	private final int INDEX_TASK_ID = 0;
	private final int INDEX_FIELDS = 1;
	private final int INDEX_NEW_DATAS = 2;  
	
	private static final String FIELD_PRIORITY = "priority";
	private static final String FIELD_TITLE = "title";	
	private static final String FIELD_LABEL = "label";
	private static final String FIELD_START_DATE = "start date";
	private static final String FIELD_END_DATE = "end date";

	private static final String[] FIELD_ARRAY = {FIELD_PRIORITY, FIELD_TITLE, 
												 FIELD_LABEL, 
												 FIELD_START_DATE, FIELD_END_DATE};
	
	private Long taskID;
	private String field;
	private String newData;
	private Date newDate = null;
	private String taskTitle=null;
	public static final String ID_ERROR_MESSAGE = "ID MUST BE NUMBER";
	public static final String FIELD_ERROR_MESSAGE = "THERE IS NO SUCH FIELD";
	
	boolean correctId;
	boolean correctField;
	boolean correctData;
	String dataErrorMsg;
	
	private ArrayList<String> argsArray;
	
	
	public EditRecurringParser(String userCommand){
		super(userCommand);
		commandArgumentsString = commandArgumentsString.trim();
		correctId = extractTaskID();
		correctField = extractFields();
		correctData = extractNewData();
	}
	
	public Command parse(){
		if(!correctId){
			return new InvalidCommand(ID_ERROR_MESSAGE);
		}
		if(!correctField){
			return new InvalidCommand(String.format(FIELD_ERROR_MESSAGE));
		}
		if(!correctData){
			return new InvalidCommand(dataErrorMsg);
		}
//		if (isEditingDateField()){
//			return new EditCommand(taskID, field, newDate); 
//		}
		else{
			return new EditRecurringCommand(taskID, field, newData);
		}
	}
 
	private boolean isEditingDateField() {
		return (newDate!=null);
	}
	
	public boolean extractTaskID(){
		if(hasNoArguments){
			return false;
		}
		String[] arguments = commandArgumentsString.split(" ",2);
		if (arguments.length == 0||arguments.length == 1 ){
			return false;
		}
		String taskIDString = arguments[0];				
		commandArgumentsString = arguments[1];
		
		try{
			taskID = Long.parseLong(taskIDString);
			
		} catch (Exception e){
			
		}
		if(taskID == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public boolean extractFields(){
		if(commandArgumentsString == null || commandArgumentsString.equals("")){
			return false;
		}
		for (String currField: FIELD_ARRAY){
			if (commandArgumentsString.indexOf(currField) == 0){
				field=currField;
				commandArgumentsString = commandArgumentsString.replace(field, "").trim();
				return true;
			}
		}
		return false;
	}
	
	public boolean extractNewData(){
		newData = commandArgumentsString;
		if(field == null){
			dataErrorMsg = "Please enter a vaild argument";
			return false;
		}
		if (field.equals("start date") || field.equals("end date") ){
			newDate = getDate(newData); 
			if(newDate != null){
				
				return true;
			}else{
				dataErrorMsg = "Please enter a vaild date";
				return false;
			}
			
		}		
		if(field == FIELD_PRIORITY){
			if (field != null) {
				if (newData.equalsIgnoreCase("h") || newData.equalsIgnoreCase("high")) {
					newData = POMPOM.PRIORITY_HIGH;
					return true;
				} else if (newData.equalsIgnoreCase("m") || newData.equalsIgnoreCase("med")
						|| newData.equalsIgnoreCase("med")) {
					newData = POMPOM.PRIORITY_MED;
					return true;
				} else if (newData.equalsIgnoreCase("l") || newData.equalsIgnoreCase("low")) {
					newData = POMPOM.PRIORITY_LOW;
					return true;
				} else {
					dataErrorMsg = "Priority only can be set to high medium low!";
					return false;
				}
			} 
			else {
				dataErrorMsg = "Priority only can be set to high medium low!";
				return false;
			}
		}
		if(hasNoArguments){
			dataErrorMsg = "No Arguments";
			return false;
		}
		return true;
	}

	private Date getDate(String data) {
		return POMPOM.timeParser.parseSyntax(data).get(0).getDates().get(0);
	}
	
	public String getField(){
		return field;
	}
	
	public String getNewData(){
		return newData;
	}
	
}
