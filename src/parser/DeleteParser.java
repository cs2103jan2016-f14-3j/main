package parser;

import command.Command;
import command.DelCommand;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

/**
 *  @@author A0121760R
 *
 */
public class DeleteParser extends ArgsParser{
	
	private static final String MESSAGE_INVALID_ID = "The task ID is invalid!";
	
	private long itemID;
	private Command InvalidCommand = null;
	
	public DeleteParser(String userCommand){
		
		super(userCommand);
		setItemId();
	}
	
	/**
	 * This method will return the appropriate Command to be processed
	 * by the Command class.
	 * 
	 * @return DelCommand() if the ID is an integer. InvalidCommand if it is not.
	 */
	public Command parse(){
		if (isNullInvalidCommand()){
			return new DelCommand(itemID);
		} else{
			return InvalidCommand;
		}
	}
	
	/**
	 * This method attempts to see if the ID is a valid integer or not. If it is,
	 * the itemID field is set. Else, the invalidCommand field will be set.
	 */
	public void setItemId(){
		try{
			//checks if itemID is an integer.
			System.out.println(this.commandArgumentsString + "LOL");
			itemID = Integer.parseInt(commandArgumentsString);
			
		} catch (Exception e){
			//Set the invalidCommand object
			InvalidParser InvalidArgumentParser = new InvalidParser(MESSAGE_INVALID_ID);
			InvalidCommand = InvalidArgumentParser.executeCommand();
		}
	}
	
	private boolean isNullInvalidCommand() {
		return InvalidCommand == null;
	}
}
