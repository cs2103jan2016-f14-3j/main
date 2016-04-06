package parser;

import command.Command;
import command.ViewCommand;

public class ViewParser extends ArgsParser{
	
	private String view;
	
	public ViewParser(String commandArgument) {
		super(commandArgument);
		view = commandArgument;
	}
	public Command executeCommand(){
		
			return new ViewCommand(view);
		
		
	}

}