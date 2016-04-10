package parser;

import command.Command;
import command.UndoCommand;

/**
 * @author A0121760R
 *
 */
public class PathParser {
	String path;
	public PathParser(String path){
		this.path = path;
	}
//	public void boolean checkProperPath(){
//		
//	}
	public Command parse(){
		return new UndoCommand();
		
	}
	
}
