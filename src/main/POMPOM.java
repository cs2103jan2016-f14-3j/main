package main;

import java.io.IOException;

import command.Command;
import parser.Parser;
import storage.Storage;

public class POMPOM {

	private static Storage storage; 
	
	public POMPOM() {
		init();
	}
	
	private void init() {
		try {
			storage = new Storage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String execute(String input) {
		Parser parser = new Parser();
		Command command = parser.executeCommand(input);
		System.out.println(command);
		String returnMsg = command.execute();
		return returnMsg;
	}
	
	public static Storage getStorage() {
		return storage;
	}
	
	
	
}