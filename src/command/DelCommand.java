package command;

import java.util.ArrayList;

import main.POMPOM;
import utils.Task;

public class DelCommand extends Command {
	
	private static final String MESSAGE_TASK_DELTED = "%s has been deleted";	
	private static final String MESSAGE_TASK_ERROR = "Unable to delete task %s";
	private int taskID;
	private String taskTitle;
	private boolean canDelete;
	
	public DelCommand(int taskID, String taskTitle) {
		this.taskID = taskID;
		this.taskTitle = taskTitle;
	}
	
	
	public void removeTask() {
		ArrayList<Task> taskList = getTaskList();
		taskList.remove(taskID);
		POMPOM.getStorage().setTaskList(taskList);
	}
	
	public String execute() {
		canDelete = checkExists(taskID);
		if (canDelete) {
			removeTask();
			returnMsg = String.format(MESSAGE_TASK_DELTED, (taskID+". "+taskTitle));
		} else {
			returnMsg = String.format(MESSAGE_TASK_ERROR, taskID);
		}
		return returnMsg;
	}
	
}