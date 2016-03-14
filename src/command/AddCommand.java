package command;

import java.util.ArrayList;
import java.util.Date;

import javafx.concurrent.Task;
import main.POMPOM;
import utils.Item;

public class AddCommand extends Command {
	
	private static final String MESSAGE_TASK_ADDED = "Task added";	
	private Item task;
	
	public AddCommand(String title) {
		task = new Item();
		task.setTitle(title);
		task.setDescription(null);
		task.setPriority(null);
		task.setStatus(null);
		task.setLabel(null);
		task.setStartDate(null);
		task.setEndDate(null);
	}
	
	public AddCommand(String title, Date endDate) {
		task = new Item();
		task.setTitle(title);
		task.setDescription(null);
		task.setPriority(null);
		task.setStatus(null);
		task.setLabel(null);
		task.setStartDate(null);
		task.setEndDate(endDate);
	}
	
	public AddCommand(String title, String description, String priority, 
			String status, String label, Date startDate, Date endDate) {

		task = new Item();
//		task.setId(getTaskList().size()+1);
		task.setTitle(title);
		task.setDescription(description);
		task.setPriority(priority);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		
	}
	
	private void storeTask() {
		POMPOM.getStorage().addItemWithId(task);
	}
		
	public String execute() {
		returnMsg = MESSAGE_TASK_ADDED;
		storeTask();
		return returnMsg;
	}
	
}