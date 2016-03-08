package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class UserTaskList {
	private String userName;
	private Task[] taskArray;

	public UserTaskList(String userName, Task[] taskArray) {
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

	public Task[] getTaskArray() {
		return taskArray;
	}

	public void setTaskArray(Task[] taskArray) {
		this.taskArray = taskArray;
	}

	public ArrayList<Task> getTaskArrayList() {
		if (taskArray == null)
			return null;
		return new ArrayList<Task>(Arrays.asList(taskArray));
	}

	public void printInfo() {
		if (userName == null)
			System.out.println("User not set");
		if (getTaskArray() == null){
			System.out.println("No Task");
			return;
		}

		System.out.println("UserName: " + userName);
		for (int i = 0; i < taskArray.length; i++) {
			taskArray[i].printInfo();		

		}

	}

}