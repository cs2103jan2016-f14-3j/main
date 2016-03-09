package command;

import java.util.ArrayList;
import java.util.Date;

import main.POMPOM;
import utils.Item;

public class EditCommand extends Command {
	
	private static final String MESSAGE_TASK_EDITED = "%s was successfully editted";	
	private static final String MESSAGE_TASK_ERROR = "Unable to edit task %s";
	
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_PRIORITY = "priority";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_LABEL = "label";
	private static final String FIELD_START_DATE = "start date";
	private static final String FIELD_END_DATE = "end date";
	
	private int taskId;
	private String field;
	private String newData;
	private Date newDate;
	private Item task;
	private boolean canEdit;
	
	public EditCommand(int taskId, String field, String newData) {
		this.taskId = taskId;
		this.field = field;
		this.newData = newData;
	}
	
	public EditCommand(int taskId, String field, Date newDate) {
		this.taskId = taskId;
		this.field = field;
		this.newDate = newDate;
	}
	
	private void updateChanges() {		
		
		switch (field.toLowerCase()) {
		case FIELD_TITLE:
			task.setTitle(newData);
			break;
		case FIELD_DESCRIPTION:
			task.setDescription(newData);
			break;
		case FIELD_PRIORITY:
			task.setPriority(newData);
			break;
		case FIELD_STATUS:
			task.setStatus(newData);
			break;
		case FIELD_LABEL:
			task.setLabel(newData);
			break;
		case FIELD_START_DATE:
			task.setStartDate(newDate);
			break;
		case FIELD_END_DATE:
			task.setEndDate(newDate);
			break;
		}
		
	}

	
	public String execute() {
		canEdit = checkExists(taskId);
		this.task = getTask(taskId);
		if (canEdit) {
			updateChanges();
			returnMsg = String.format(MESSAGE_TASK_EDITED, "Task"+taskId);
		} else {
			returnMsg = MESSAGE_TASK_ERROR;
		}
		return returnMsg;
	}
	
}