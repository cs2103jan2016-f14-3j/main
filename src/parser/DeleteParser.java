package parser;

import java.util.Date;
import command.Command;
import command.DelCommand;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class DeleteParser extends ArgsParser{
	
	//Array contents:
	//[0: Item ID]
	
	private final int INDEX_ITEM_ID = 0;

	
	private String itemID;

	
	private SimpleDateFormat dateFormatter;
	
	public DeleteParser(String userCommand){
		super(userCommand);
		itemID = getItemID();
	}
	
	public Command executeCommand(){
		return new DelCommand(Integer.parseInt(itemID),itemID); 
	}
	
	public String getItemID(){
		return this.argsArray[INDEX_ITEM_ID];
	}	
}
