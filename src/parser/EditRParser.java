package parser;

import java.util.ArrayList;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import command.Command;
import command.EditCommand;
import command.EditRecurringCommand;

public class EditRParser extends ArgsParser{
	
	private final int INDEX_TASK_ID = 0;
	private final int INDEX_FIELDS = 1;
	private final int INDEX_NEW_DATAS = 2; 
	
	private Long taskID;
	private String field;
	private String newData;
	
	private ArrayList<String> argsArray;
	
	
	public EditRParser(String userCommand){
		super(userCommand);
		
		argsArray = new ArrayList<String>(Arrays.asList(commandArgumentsString.split(" ")));
		extractTaskID();
		System.out.println(argsArray.toString());
		extractFields();
		System.out.println(argsArray.toString());
		extractNewData();
	}
	
	public Command executeCommand(){
		return new EditRecurringCommand(taskID, field, newData); 
	}
	
	public void extractTaskID(){
		try{
			taskID = Long.parseLong(argsArray.remove(0));
		} catch (Exception e){
			taskID = (long) -1;
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
