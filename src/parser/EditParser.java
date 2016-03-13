package parser;

import command.Command;
import command.EditCommand;

public class EditParser extends ArgsParser{
	
	private final int INDEX_TASK_ID = 0;
	private final int INDEX_FIELDS = 1;
	private final int INDEX_NEW_DATAS = 2; 
	
	private int taskID;
	private String unparsedFields;
	private String unparsedData;
	
	private String[] fieldsArray;
	private String[] newDataArray;
	private String[] argsArray;
	
	
	public EditParser(String userCommand){
		super(userCommand);
		
		argsArray = commandArgumentsString.split(" ");
		taskID = getTaskID();
		unparsedFields = getFields();
		unparsedData = getNewData();
		
		fieldsArray = unparsedFields.split(",");
		newDataArray = unparsedData.split(",");
	}
	
	public Command executeCommand(){
		return new EditCommand(taskID, fieldsArray[0], newDataArray[0]); 
	}
	
	public int getTaskID(){
		return Integer.parseInt(argsArray[INDEX_TASK_ID]);
	}
	
	public String getFields(){
		return argsArray[INDEX_FIELDS];
	}
	
	public String getNewData(){
		return argsArray[INDEX_NEW_DATAS];
	}
	
	public String[] parseFields(String input){
		return input.split(",");
	}
	
	public String[] parseNewData(String input){
		return input.split(",");
	}
	
}
