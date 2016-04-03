package command;

import java.util.ArrayList;
import java.util.logging.Logger;

import main.POMPOM;
import utils.Item;
/**
 * @@author wen hao
 *
 */
public abstract class Command {
	
	protected String returnMsg = "";
	
	public static Logger logger = Logger.getLogger("Command");
	
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
