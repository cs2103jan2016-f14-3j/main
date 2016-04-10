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
		
		Item toDelete =null;
		try {
			toDelete = getTask(taskId);
			
		} catch (IndexOutOfBoundsException e) {
			
		}
		if(toDelete==null){
			return false;
		}else{
			return true;
		}
	}
	
	protected void showCorrectTab(Item item) {
		
		if (item.getType().toLowerCase().equals(POMPOM.LABEL_EVENT.toLowerCase())) {
			if (item.getStatus().equals(POMPOM.STATUS_COMPLETED)) {
				POMPOM.setCurrentTab(POMPOM.LABEL_COMPLETED_EVENT);
			} else {
				POMPOM.setCurrentTab(POMPOM.LABEL_EVENT);
			}
		} else {
			if (item.getStatus().equals(POMPOM.STATUS_COMPLETED)) {
				POMPOM.setCurrentTab(POMPOM.LABEL_COMPLETED_TASK);
			} else {
				POMPOM.setCurrentTab(POMPOM.LABEL_TASK);
			}
		}
		
	}
	
	public abstract String execute();
	
}
