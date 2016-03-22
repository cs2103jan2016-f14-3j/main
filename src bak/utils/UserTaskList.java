package utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author A0121628L
 *
 */
public class UserTaskList {
	private String userName;
	private long IdCounter;
	private ArrayList<Item> taskArray;
	
	public UserTaskList(String userName, ArrayList<Item> taskArray) {
		this.userName = userName;
		this.taskArray = taskArray;
	}

	public UserTaskList() {
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<Item> getTaskArray() {
		return taskArray;
	}

	public void setTaskArray(ArrayList<Item> taskArray) {
		this.taskArray = taskArray;
	}
	public long getIdCounter() {
		return IdCounter;
	}

	public void setIdCounter(long idCounter) {
		IdCounter = idCounter;
	}
	// Debugging Method.
	public void printInfo() {
		if (userName == null)
			System.out.println("User not set");
		if (getTaskArray() == null){
			System.out.println("No Task");
			return;
		}

		System.out.println("UserName: " + userName);
		for (int i = 0; i < taskArray.size(); i++) {
			taskArray.get(i).printInfo();		

		}

	}
	

}