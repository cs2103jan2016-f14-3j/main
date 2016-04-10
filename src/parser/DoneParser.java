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
	private Long itemID;
	private Command outputCommand = null; 
	
	public DoneParser(String userCommand){
		super(userCommand);
		getItemId();
	}
	
	public Command executeCommand(){
		if (outputCommand == null){
			return new EditCommand(itemID, "status", POMPOM.STATUS_COMPLETED);
		} else{
			return outputCommand;
		}
		
	}
	
	public void getItemId(){
		try{
			itemID = Long.parseLong(commandArgumentsString);
		} catch (Exception e){
			String returnMsg = String.format(INVALID_DONE_ARGUMENT_RETURN_MESSAGE, commandArgumentsString);
			InvalidParser InvalidArgumentParser = new InvalidParser(returnMsg);
			outputCommand = InvalidArgumentParser.executeCommand();
			logger.log(Level.INFO,"tried to parse '"+commandArgumentsString+"' into integer but failed.");
		}
	}	
}
