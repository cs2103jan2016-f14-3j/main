package parser;

import command.Command;
import command.InvalidCommand;

/**
 * @@author William
 *
 */


public class InvalidParser {
	
	private String invalidCommand;
	public InvalidParser(String userCommand){
		invalidCommand = userCommand;
	}
	
	public Command executeCommand(){
		return new InvalidCommand(invalidCommand);
	}
}
