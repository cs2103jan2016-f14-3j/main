package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

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
	private static Stack<Command> undoStack;
	public static PrettyTimeParser timeParser;
	public static ArrayList<Item> searchList;
	public static String changedTab;
	


	public static String getChangedTab() {
		return changedTab;
	}

	public static void setChangedTab(String setTab) {
		POMPOM.changedTab = setTab;
	}

	public static ArrayList<Item> getSearchList() {
		return searchList;
	}

	public static void setSearchList(ArrayList<Item> searchList) {
		POMPOM.searchList = searchList;
	}

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
			timeParser = new PrettyTimeParser();
			timeParser.parseSyntax("next year");
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
		Parser parser = new Parser();
		Command command = parser.executeCommand(input);
		System.out.println(command);
		String returnMsg = command.execute();
		return returnMsg;
	}
	public static String executeCommand(Command executable){
		String returnMsg = executable.execute();
		return returnMsg;
	}
	public static PrettyTimeParser getTimeParser() {
		return timeParser;
	}

	public static void setTimeParser(PrettyTimeParser timeParser) {
		POMPOM.timeParser = timeParser;
	}
	public static Storage getStorage() {
		return storage;
	}
	public static void saveSettings(String storageFilePath) throws IOException{
		storage.setStorageFilePath(storageFilePath);
		storage.saveSettings();
	}

	public static Stack getUndoStack() {
		return undoStack;
	}
	
}