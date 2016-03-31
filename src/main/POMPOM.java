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

/**
 * @@author wen hao
 *
 */
public class POMPOM {

	public static final String STATUS_PENDING = "pending";
	public static final String STATUS_ONGOING = "ongoing";
	public static final String STATUS_COMPLETED = "completed";
	public static final String STATUS_OVERDUE = "overdue";
	public static final String STATUS_FLOATING = "floating";

	public static final String LABEL_TASK = "Task";
	public static final String LABEL_COMPLETED_TASK = "CompletedTask";
	public static final String LABEL_EVENT = "Event";
	public static final String LABEL_COMPLETED_EVENT = "CompletedEvent";
	public static final String LABEL_SEARCH = "Search";

	private static Storage storage;
	private static Stack<Command> undoStack;
	public static PrettyTimeParser timeParser;
	public static ArrayList<Item> searchList;
	public static String currentTab;

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

	public static void refreshStatus() {
		ArrayList<Item> taskList = storage.getTaskList();
		Date currentDate = new Date();

		for (int i = 0; i < taskList.size(); i++) {
			Item currentTask = taskList.get(i);
			Date taskStartDate = currentTask.getStartDate();
			Date taskEndDate = currentTask.getEndDate();

			if (taskStartDate == null && taskEndDate == null) {
				currentTask.setStatus(STATUS_FLOATING);

			} else if (taskStartDate == null) {

				if (currentDate.before(taskEndDate)) {
					currentTask.setStatus(STATUS_ONGOING);
				} else if (!currentTask.getStatus().equals(STATUS_COMPLETED)) {
					currentTask.setStatus(STATUS_OVERDUE);
				}

			} else if (taskEndDate == null) {

				if (taskStartDate.after(currentDate)) {
					currentTask.setStatus(STATUS_PENDING);
				} else if (taskStartDate.before(currentDate)
						&& isNotCompleted(currentTask)) {
					currentTask.setStatus(STATUS_ONGOING);
				}

			} else if (currentDate.before(taskStartDate)) {
				currentTask.setStatus(STATUS_PENDING);

			} else if (currentDate.compareTo(taskStartDate) >= 0
					&& currentDate.before(taskEndDate)
					&& isNotCompleted(currentTask)) {
				currentTask.setStatus(STATUS_ONGOING);

			} else if (currentDate.after(taskEndDate)
					&& isNotCompleted(currentTask)) {
				currentTask.setStatus(STATUS_OVERDUE);

			}
		}
	}

	public String execute(String input) {
		Parser parser = Parser.getInstance();
		Command command = parser.executeCommand(input);
		String returnMsg = command.execute();
		refreshStatus();
		return returnMsg;
	}

	public static String executeCommand(Command executable) {
		String returnMsg = executable.execute();
		refreshStatus();
		return returnMsg;
	}

	private static boolean isNotCompleted(Item item) {

		return !item.getStatus().equals(STATUS_COMPLETED);

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

	public static Stack<Command> getUndoStack() {
		return undoStack;
	}

	public static void saveSettings(String storageFilePath) throws IOException {
		storage.setStorageFilePath(storageFilePath);
		storage.saveSettings();
	}

	public static String getCurrentTab() {
		return POMPOM.currentTab;
	}

	public static void setCurrentTab(String setTab) {
		POMPOM.currentTab = setTab;
	}

	public static ArrayList<Item> getSearchList() {
		return searchList;
	}

	public static void setSearchList(ArrayList<Item> searchList) {
		POMPOM.searchList = searchList;
	}

}