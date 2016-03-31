package parser;

import command.Command;
import command.InvalidCommand;

public class InvalidParser {
	
	private String invalidCommand;
	public InvalidParser(String userCommand){
		invalidCommand = userCommand;
	}
	
	public Command executeCommand(){
		return new InvalidCommand(invalidCommand);
	}
}
