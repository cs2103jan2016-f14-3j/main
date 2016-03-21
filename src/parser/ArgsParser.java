package parser;
import command.InvalidCommand;
import command.Command;


public class ArgsParser {
	
	protected boolean hasNoArguments=false;
	protected String commandArgumentsString;
	
	public ArgsParser(String commandArguments){
		commandArgumentsString = commandArguments;
		checkForAnyArguments();
	}

	private void checkForAnyArguments() {
		if (commandArgumentsString.equals("")){	
			hasNoArguments=true;
		}
	}
	
	public Command invalidArgs(){
		return new InvalidCommand(commandArgumentsString);
	}
}
