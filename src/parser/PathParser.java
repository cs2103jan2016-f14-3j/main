package parser;

import command.Command;
import command.UndoCommand;

public class PathParser {
	private String path;
	public PathParser(String path){
		this.path = path;
	}
	public Command parse(){
		return new UndoCommand();
		
	}
	
}
