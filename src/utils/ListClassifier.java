package utils;

import java.util.ArrayList;

/**
 * @@author A0121628L
 *
 */
public class ListClassifier {

	private ArrayList<Item> taskArrayList;

	//
	public static ArrayList<Item> getDoneTaskList(ArrayList<Item> lst) {
		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < lst.size(); i++) {
			Item currentTask = lst.get(i);
			// Remove this line after proper init of task and evetns
			if (currentTask.getType() == null)
				continue;
			if (currentTask.getType().toLowerCase().equals("task")
					&& currentTask.getStatus().toLowerCase().equals("completed")) {
				result.add(currentTask);
			}
		}
		return result;
	}

	public static ArrayList<Item> getTaskList(ArrayList<Item> lst) {
		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < lst.size(); i++) {
			Item currentTask = lst.get(i);
			// Remove this line after proper init of task and evetns
			if (currentTask.getType() == null)
				continue;
			if (currentTask.getType().toLowerCase().equals("task")
					&& !currentTask.getStatus().toLowerCase().equals("completed")) {
				result.add(currentTask);
			}
		}
		return result;
	}

	//
	public static ArrayList<Item> getEventList(ArrayList<Item> lst) {
		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < lst.size(); i++) {
			Item currentTask = lst.get(i);
			if (currentTask.getType() == null)
				continue;
			if (currentTask.getType().toLowerCase().equals("event")
					&& !currentTask.getStatus().equals("completed")) {
				result.add(currentTask);
			}
		}
		return result;
	}

	public static ArrayList<Item> getDoneEventList(ArrayList<Item> lst) {
		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < lst.size(); i++) {
			Item currentTask = lst.get(i);
			if (currentTask.getType() == null)
				continue;
			if (currentTask.getType().toLowerCase().equals("event")
					&& currentTask.getStatus().equals("completed")) {
				result.add(currentTask);
			}
		}
		return result;
	}

	public ArrayList<Item> getPendingList() {
		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < taskArrayList.size(); i++) {
			Item currentTask = taskArrayList.get(i);
			if (currentTask.getStatus().toLowerCase().equals("pending")) {
				result.add(currentTask);
			}
		}
		return result;
	}

	public ArrayList<Item> getCompletedList() {

		ArrayList<Item> result = new ArrayList<Item>();

		for (int i = 0; i < taskArrayList.size(); i++) {
			Item currentTask = taskArrayList.get(i);
			if (currentTask.getStatus().toLowerCase().equals("completed")) {
				result.add(currentTask);
			}
		}
		return result;
	}

	public ArrayList<Item> getOverdueList() {
		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < taskArrayList.size(); i++) {
			Item currentTask = taskArrayList.get(i);
			if (currentTask.getStatus().toLowerCase().equals("completed")) {
				result.add(currentTask);
			}
		}
		return result;
	}

	public ArrayList<Item> getDeletedList() {
		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < taskArrayList.size(); i++) {
			Item currentTask = taskArrayList.get(i);
			if (currentTask.getStatus().toLowerCase().equals("deleted")) {
				result.add(currentTask);
			}
		}

		return result;

	}

}
