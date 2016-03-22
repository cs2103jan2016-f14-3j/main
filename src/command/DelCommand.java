package command;

import java.util.ArrayList;

import main.POMPOM;
import utils.Item;

public class DelCommand extends Command {
	
	private static final String MESSAGE_TASK_DELETED = "%s has been deleted";	
	private static final String MESSAGE_TASK_ERROR = "Unable to delete task %s";
	private long taskId;
	private boolean canDelete;
	private Item toDelete;
	
	public DelCommand(long taskId) {
		this.taskId = taskId;
	}
	
	private void removeTask() {
		ArrayList<Item> taskList = getTaskList();
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getId() == taskId) {
				taskList.remove(i);
			}
		}
		POMPOM.getStorage().setTaskList(taskList);
	}
	
	private Command createCounterAction() {
		toDelete = getTask(taskId);
		AddCommand counterAction = new AddCommand(toDelete.getTitle(), toDelete.getDescription(), toDelete.getPriority(), 
				toDelete.getStatus(), toDelete.getLabel(), toDelete.getStartDate(), toDelete.getEndDate());
		return counterAction;
	}
	
	private void updateUndoStack() {
		Command counterAction = createCounterAction();
		POMPOM.getUndoStack().push(counterAction);
	}
	
	public String execute() {
		canDelete = checkExists(taskId);
		if (canDelete) {
			updateUndoStack();
			removeTask();
			returnMsg = String.format(MESSAGE_TASK_DELETED, (taskId+". "));
		} else {
			returnMsg = String.format(MESSAGE_TASK_ERROR, taskId);
		}
		return returnMsg;
	}
	
}