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
	
	private Long itemID;
	private Command outputCommand = null;
	
	public DeleteParser(String userCommand){
		super(userCommand);
		getItemId();
	}
	
	public Command parse(){
		if (outputCommand == null){
			return new DelCommand(itemID);
		} else{
			return outputCommand;
		}
		
	}
	
	public void getItemId(){
		try{
			itemID = Long.parseLong(commandArgumentsString);
		} catch (Exception e){
			InvalidParser InvalidArgumentParser = new InvalidParser(commandArgumentsString);
			outputCommand = InvalidArgumentParser.executeCommand();
			logger.log(Level.INFO,"tried to parse '"+commandArgumentsString+"' into integer but failed.");
		}
	}	
}
