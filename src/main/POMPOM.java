package main;

import java.io.IOException;
import java.util.Stack;

import command.Command;
import parser.Parser;
import storage.Storage;

public class POMPOM {

	private static Storage storage; 
	public static Stack<Command> undoStack;
	
	public POMPOM() {
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init() throws IOException {
		try {
			storage = new Storage();
			storage.init();
			undoStack = new Stack<Command>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String execute(String input) {
		Parser parser = new Parser();
		Command command = parser.executeCommand(input);
		String returnMsg = command.execute();
		return returnMsg;
	}
	
	public static Storage getStorage() {
		return storage;
	}

	public static Stack getUndoStack() {
		return undoStack;
	}
	
}