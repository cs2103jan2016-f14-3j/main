package command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.POMPOM;
import utils.Item;
/**
 * @@author wen hao
 *
 */
public class DelCommand extends Command {

	private static final String MESSAGE_TASK_DELETED = "%1s has been deleted from %2s";
	private static final String MESSAGE_TASK_ERROR = "Unable to delete task %s";
	private long taskId;
	private String taskTitle;
	private boolean isById;
	private boolean isUndo;
	private boolean canDelete;
	private Item toDelete;

	public DelCommand(long taskId) {
		this.taskId = taskId;
		this.isById = true;
		isUndo = false;

		logger.log(Level.INFO, "DelCommand with id initialized");
	}
	
	public DelCommand(String taskTitle) {
		this.taskTitle = taskTitle;
		this.isById = false;
		isUndo = false;

		logger.log(Level.INFO, "DelCommand with title initialized");
	}

	public DelCommand(long taskId, boolean isUndo) {
		this.taskId = taskId;
		this.isUndo = isUndo;

		logger.log(Level.INFO, "Counter action DelCommand initialized");
	}

	private void removeTask() {
		if (isById) {
			
			ArrayList<Item> taskList = getTaskList();
			for (int i = 0; i < taskList.size(); i++) {
				if (taskList.get(i).getId() == taskId) {
					taskList.remove(i);
				}
			}
			POMPOM.getStorage().setTaskList(taskList);
	
		} else {
			
			ArrayList<Item> taskList = getTaskList();
			for (int i = 0; i < taskList.size(); i++) {
				if (taskList.get(i).getTitle().toLowerCase().equals(taskTitle.toLowerCase())) {
					taskList.remove(i);
				}
			}
			POMPOM.getStorage().setTaskList(taskList);
			
		}
	}

	private Command createCounterAction() {
		toDelete = getTask(taskId);
		AddCommand counterAction = new AddCommand(toDelete.getId(), toDelete.getType(), toDelete.getTitle(),
				toDelete.getDescription(), toDelete.getPriority(), toDelete.getStatus(), toDelete.getLabel(),
				toDelete.getStartDate(), toDelete.getEndDate());
		return counterAction;
	}

	private void updateUndoStack() {
		Command counterAction = createCounterAction();
		POMPOM.getUndoStack().push(counterAction);
	}

	public String execute() {
		canDelete = checkExists(taskId);
		if (canDelete) {

			if (!isUndo) {
				updateUndoStack();
				returnMsg = String.format(MESSAGE_TASK_DELETED, (taskId + "."), toDelete.getType());

				if (toDelete.getType().equals(POMPOM.LABEL_EVENT)) {
					if (toDelete.getStatus().equals(POMPOM.STATUS_OVERDUE)) {
						POMPOM.setCurrentTab(POMPOM.LABEL_COMPLETED_EVENT);
					} else {
						POMPOM.setCurrentTab(POMPOM.LABEL_EVENT);
					}
				} else {
					if (toDelete.getStatus().equals(POMPOM.STATUS_OVERDUE)) {
						POMPOM.setCurrentTab(POMPOM.LABEL_COMPLETED_TASK);
					} else {
						POMPOM.setCurrentTab(POMPOM.LABEL_TASK);
					}
				}
			}

			removeTask();
			logger.log(Level.INFO, "DelCommand has be executed");
			
		} else {
			returnMsg = String.format(MESSAGE_TASK_ERROR, taskId);
		}
		return returnMsg;
	}

}