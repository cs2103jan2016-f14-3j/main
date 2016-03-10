package parser;

import command.Command;
import command.ExitCommand;

public class ExitParser {
	public Command executeCommand(){
		return ExitCommand();
	}
}
