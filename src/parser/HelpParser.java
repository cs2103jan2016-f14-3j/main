package parser;

import command.Command;
import command.HelpCommand;
/**
 *  @@author A0121760R
 *
 */
public class HelpParser{
	public HelpParser(){
		 
	}
	public Command parse(){
		return new HelpCommand(); 
	}
}
