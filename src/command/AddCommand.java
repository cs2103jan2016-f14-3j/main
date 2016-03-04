package command;

import java.util.ArrayList;
import java.util.Date;

import main.POMPOM;
import utils.Task;

public class AddCommand extends Command {
	
	private static final String MESSAGE_TASK_ADDED = "Task added";	
	private Task task;
	
	public AddCommand(String title, String description, String priority, 
			String status, String label, Date startDate, Date endDate) {
		task = new Task();
		task.setTitle(title);
		task.setDescription(description);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
	}
	
	public void storeTask() {
		ArrayList<Task> taskList = getTaskList();
		taskList.add(task);
		POMPOM.getStorage().setTaskList(taskList);
	}
	
	public String execute() {
		returnMsg = MESSAGE_TASK_ADDED;
		storeTask();
		return returnMsg;
	}
	
}