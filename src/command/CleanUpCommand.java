package command;

import java.util.ArrayList;

import utils.Item;
/**
 * @@author wen hao
 *
 */
public class CleanUpCommand extends Command {

	private static final String MESSAGE_CLEAN_UP = "The task list has been cleaned up; "
			+ "tasks with deleted status are removed and task ID are updated.";
	ArrayList<Item> taskList;
	
	public CleanUpCommand() {
		this.taskList = getTaskList();
	}
	

	public String execute() {
		
		int removeCount = 0;
		for (int i = 0; i < taskList.size(); i++) {
			Item currentTask = getTask(i);
			if (currentTask.getStatus().equals("deleted")) {
				taskList.remove(i);
				removeCount++;
			} else {
//				currentTask.setId(i+1-removeCount);
			}
		}
		
		returnMsg = MESSAGE_CLEAN_UP;
		return returnMsg;
		
	}
	
}
