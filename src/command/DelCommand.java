package command;

import java.util.ArrayList;

import main.POMPOM;
import utils.Item;

public class DelCommand extends Command {
	
	private static final String MESSAGE_TASK_DELETED = "%s has been deleted";	
	private static final String MESSAGE_TASK_ERROR = "Unable to delete task %s";
	private int taskId;
	private String taskTitle;
	private boolean canDelete;
	
	public DelCommand(int taskId) {
		this.taskId = taskId-1;
	}
	
	private void removeTask() {
		ArrayList<Item> taskList = getTaskList();
		taskList.remove(taskId);
		for (int i = 0; i < taskList.size(); i++) {
			taskList.get(i).setId(i+1);
		}
		POMPOM.getStorage().setTaskList(taskList);
	}
	
	public String execute() {
		canDelete = checkExists(taskId);
		if (canDelete) {
			removeTask();
			returnMsg = String.format(MESSAGE_TASK_DELETED, (taskId+". "+taskTitle));
		} else {
			returnMsg = String.format(MESSAGE_TASK_ERROR, taskId);
		}
		return returnMsg;
	}
	
}