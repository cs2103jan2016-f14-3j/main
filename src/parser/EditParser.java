package parser;

import command.Command;
import command.EditCommand;

public class EditParser extends ArgsParser{
	
	//Array content:
	//[0: Task ID][1: Fields][2: New Data]
	
	private final int INDEX_TASK_ID = 0;
	private final int INDEX_FIELDS = 1;
	private final int INDEX_NEW_DATAS = 2; 
	
	private int taskID;
	private String unparsedFields;
	private String unparsedData;
	
	private int numberOfChanges;
	
	private String[] fieldsArray;
	private String[] newDataArray;
	
	
	public EditParser(String userCommand){
		super(userCommand);
		taskID = getTaskID();
		unparsedFields = getFields();
		unparsedData = getNewData();
		
		fieldsArray = unparsedFields.split(",");
		newDataArray = unparsedData.split(",");
		numberOfChanges = fieldsArray.length;
	}
	
	public Command executeCommand(){
		return new EditCommand(taskID, fieldsArray[0], newDataArray[0]); 
	}
	
	public int getTaskID(){
		return Integer.parseInt(this.argsArray[INDEX_TASK_ID]);
	}
	
	public String getFields(){
		return this.argsArray[INDEX_FIELDS];
	}
	
	public String getNewData(){
		return this.argsArray[INDEX_NEW_DATAS];
	}
	
	public String[] parseFields(String input){
		return input.split(",");
	}
	
	public String[] parseNewData(String input){
		return input.split(",");
	}
	
}
