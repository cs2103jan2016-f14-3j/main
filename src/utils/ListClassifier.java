package utils;

import java.util.ArrayList;

import main.POMPOM;

public class ListClassifier {

	private ArrayList<Item> taskArrayList; 
	
	public ListClassifier() {
		this.taskArrayList = POMPOM.getStorage().getTaskList();
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
			
			if (currentTask.getStatus().toLowerCase().equals("overdue")) {
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
