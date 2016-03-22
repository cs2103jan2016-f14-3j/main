package command;

import java.util.ArrayList;

import main.POMPOM;
import utils.Item;

public abstract class Command {

	protected static final String STATUS_PENDING = "pending";
	protected static final String STATUS_ONGOING = "ongoing";
	protected static final String STATUS_COMPLETED = "completed";
	protected static final String STATUS_OVERDUE = "overdue";
	protected String returnMsg = "";
	
	public Command() {
		
	}
	
	protected Item getTask(long taskId) {
		ArrayList<Item> taskList = getTaskList(); 
		for (int i  = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getId() == taskId) {
				return taskList.get(i);
			}
		}
		return null;
	}
	
	protected ArrayList<Item> getTaskList() {
		return POMPOM.getStorage().getTaskList();
	}
	
	protected boolean checkExists(long taskId) {
		boolean exists;
		try {
			Item toDelete = getTask(taskId);
			exists = true;
		} catch (IndexOutOfBoundsException e) {
			exists =  false;
		}
		return exists;
	}
	
	public abstract String execute();
	
}
