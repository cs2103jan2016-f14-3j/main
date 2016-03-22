package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import command.Command;
import parser.Parser;
import storage.Storage;
import utils.Item;

public class POMPOM {
	
	protected static final String STATUS_PENDING = "pending";
	protected static final String STATUS_ONGOING = "ongoing";
	protected static final String STATUS_COMPLETED = "completed";
	protected static final String STATUS_OVERDUE = "overdue";
	
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
			refreshStatus();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void refreshStatus() {
		ArrayList<Item> taskList =  storage.getTaskList();
		Date currentDate = new Date();
		
		for (int i = 0; i < taskList.size(); i++) {
			Item currentTask = taskList.get(i);
			Date taskStartDate = currentTask.getStartDate();
			Date taskEndDate = currentTask.getEndDate();
			
			if (currentDate.compareTo(taskStartDate) < 0) {
				currentTask.setStatus(STATUS_PENDING);
				
			} else if (currentDate.compareTo(taskStartDate) >= 0 && currentDate.compareTo(taskEndDate) < 0) {
				currentTask.setStatus(STATUS_ONGOING);
				
			} else if (currentDate.compareTo(taskEndDate) > 0 && !currentTask.getStatus().equals(STATUS_COMPLETED)) {
				currentTask.setStatus(STATUS_OVERDUE);

			}
		}
	}
	
	public String execute(String input) {
		Parser parser = Parser.getInstance();
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