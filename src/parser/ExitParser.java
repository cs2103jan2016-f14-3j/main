package parser;

import command.Command;
import command.ExitCommand;
/**
 *  @@author A0121760R
 *
 */
public class ExitParser {
	public Command executeCommand(){
		return new ExitCommand();
	}
}
