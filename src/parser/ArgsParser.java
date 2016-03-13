package parser;
import command.InvalidCommand;
import command.Command;



public class ArgsParser {
	boolean noArgs=false;
	String commandArgumentsString;
	public ArgsParser(String commandArguments){
		commandArgumentsString = commandArguments;
		if (commandArgumentsString.equals("")){	
			noArgs=true;
		}
	}
	
	public Command invalidArgs(){
		return new InvalidCommand(commandArgumentsString);
	}
	
}
