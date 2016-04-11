package parser;

import command.Command;
import command.EditCommand;
import main.POMPOM;

import java.security.PublicKey;
import java.util.logging.Level;
/**
 *  @@author A0121760R
 *
 */

public class DoneParser extends ArgsParser{
	
	public static final String INVALID_DONE_ARGUMENT_RETURN_MESSAGE = "%s: Is not a valid ID Number";
	private long itemID;
	private Command invalidCommand = null; 
	
	public DoneParser(String userCommand){
		super(userCommand); 
		getItemId();
	}
	
	/**
	 * This method will return the appropriate Command to be processed
	 * by the Command class.
	 * 
	 * @return DelCommand() if the ID is an integer. InvalidCommand if it is not.
	 */
	public Command parse(){
		if (isNullInvalidCommand()){
			return new EditCommand(itemID, "status", POMPOM.STATUS_COMPLETED);
		} else{
			return invalidCommand;
		}
		
	}

	/**
	 * This method attempts to see if the ID is a valid integer or not. If it is,
	 * the itemID field is set. Else, the invalidCommand field will be set.
	 */
	public void getItemId(){
		try{
			itemID = Integer.parseInt(commandArgumentsString);
		} catch (Exception e){
			String returnMsg = String.format(INVALID_DONE_ARGUMENT_RETURN_MESSAGE, commandArgumentsString);
			InvalidParser InvalidArgumentParser = new InvalidParser(returnMsg);
			invalidCommand = InvalidArgumentParser.executeCommand();
		}
	}
	
	private boolean isNullInvalidCommand() {
		return invalidCommand == null;
	}
}
