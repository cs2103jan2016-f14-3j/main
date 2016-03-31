package command;

/**
 * @@author wen hao
 *
 */
import java.util.Date;

import main.POMPOM;
import utils.Item;

public class RestoreCommand extends Command {

	private static final String MESSAGE_TASK_RESTORED = "%s has been restored";
	private static final String MESSAGE_TASK_ERROR = "Unable to restore task %s";
	private int taskId;
	private String taskTitle;
	private boolean canDelete;

	public RestoreCommand(int taskId) {
		this.taskId = taskId - 1;
	}

	private void removeTask() {
		Item task = getTask(taskId);
		Date currentDate = new Date();
		if (task.getEndDate().compareTo(currentDate) < 0) {
			task.setStatus(POMPOM.STATUS_PENDING);
		} else {
			task.setStatus(POMPOM.STATUS_OVERDUE);
		}
	}

	public String execute() {
		canDelete = checkExists(taskId);
		if (canDelete) {
			removeTask();
			returnMsg = String.format(MESSAGE_TASK_RESTORED,
					(taskId + ". " + taskTitle));
		} else {
			returnMsg = String.format(MESSAGE_TASK_ERROR, taskId);
		}
		return returnMsg;
	}

}