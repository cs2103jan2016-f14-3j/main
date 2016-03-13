package command;

import java.util.ArrayList;

import main.POMPOM;
import utils.Item;

public abstract class Command {

	protected static final String STATUS_PENDING = "pending";
	protected static final String STATUS_COMPLETED = "completed";
	protected static final String STATUS_OVERDUE = "overdue";
	protected static final String STATUS_DELETED = "deleted";
	protected String returnMsg = "";
	
	public Command() {
		
	}
	
	protected Item getTask(int taskId) {
		return getTaskList().get(taskId);
	}
	
	protected ArrayList<Item> getTaskList() {
		return POMPOM.getStorage().getTaskList();
	}
	
	protected boolean checkExists(int taskID) {
		boolean exists;
		try {
			Item toDelete = getTask(taskID);
			exists = true;
		} catch (IndexOutOfBoundsException e) {
			exists =  false;
		}
		return exists;
	}
	
	public abstract String execute();
	
}
