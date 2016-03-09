package command;

import java.util.ArrayList;
import java.util.Date;

import main.POMPOM;
import utils.Item;

public class AddCommand extends Command {
	
	private static final String MESSAGE_TASK_ADDED = "Task added";	
	private Item task;
	
	public AddCommand(String title, String description, String priority, 
			String status, String label, Date startDate, Date endDate) {
		task = new Item();
		task.setId(getTaskList().size()+1);
		task.setTitle(title);
		task.setDescription(description);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
	}
	
	private void storeTask() {
		ArrayList<Item> taskList = getTaskList();
		taskList.add(task);
		POMPOM.getStorage().setTaskList(taskList);
	}
	
	public String execute() {
		returnMsg = MESSAGE_TASK_ADDED;
		storeTask();
		return returnMsg;
	}
	
}