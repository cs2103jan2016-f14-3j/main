package command;

import java.util.ArrayList;

import main.POMPOM;
import utils.Task;

public abstract class Command {

	protected String returnMsg = "";
	
	public Command() {
		
	}
	
	protected Task getTask(int taskID) {
		return getTaskList().get(taskID);
	}
	
	protected ArrayList<Task> getTaskList() {
		return POMPOM.getStorage().getTaskList();
	}
	
	protected boolean checkExists(int taskID) {
		boolean exists;
		try {
			Task toDelete = getTask(taskID);
			exists = true;
		} catch (IndexOutOfBoundsException e) {
			exists =  false;
		}
		return exists;
	}
	
	public abstract String execute();
	
}
