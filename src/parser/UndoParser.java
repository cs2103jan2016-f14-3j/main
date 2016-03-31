package parser;

import command.Command;
import command.DelCommand;
import command.ExitCommand;
import command.UndoCommand;


public class UndoParser {


	
	public Command executeCommand(){
		return new UndoCommand();
		
	}
	

	
}
