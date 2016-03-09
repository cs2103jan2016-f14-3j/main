package command;

import java.util.ArrayList;

import main.POMPOM;
import utils.Item;

public abstract class Command {

	protected String returnMsg = "";
	
	public Command() {
		
	}
	
	protected Item getTask(int taskID) {
		return getTaskList().get(taskID);
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
