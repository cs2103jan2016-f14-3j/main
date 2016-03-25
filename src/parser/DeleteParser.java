package parser;

import command.Command;
import command.DelCommand;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

/**
 * @@author William
 *
 */

public class DeleteParser extends ArgsParser{
	
	private int itemID;
	private Command outputCommand = null;
	
	public DeleteParser(String userCommand){
		super(userCommand);
		getItemId();
	}
	
	public Command executeCommand(){
		if (outputCommand == null){
			System.out.println(commandArgumentsString + "lol");
			return new DelCommand(itemID);
		} else{
			return outputCommand;
		}
		
	}
	
	public void getItemId(){
		try{
			itemID = Integer.parseInt(commandArgumentsString);
		} catch (Exception e){
			InvalidParser InvalidArgumentParser = new InvalidParser(commandArgumentsString);
			outputCommand = InvalidArgumentParser.executeCommand();
			logger.log(Level.INFO,"tried to parse '"+commandArgumentsString+"' into integer but failed.");
		}
	}	
}
