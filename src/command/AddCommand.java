package command;

import java.util.Date;
import static org.junit.Assert.*;
import main.POMPOM;
import utils.Item;

public class AddCommand extends Command {
	
	private static final String MESSAGE_TASK_ADDED = "Task added";	
	private Item task;
	private boolean isUndo;
	
	public AddCommand(String title) {
		task = new Item();
		isUndo = false;
		assertNotNull(title);

		Long id = POMPOM.getStorage().getIdCounter();
		task.setId(id);
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
		isUndo = false;
		assertNotNull(title);
		
		Long id = POMPOM.getStorage().getIdCounter();
		task.setId(id);
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
		isUndo = false;
		assertNotNull(title);
		
		Long id = POMPOM.getStorage().getIdCounter();
		task.setId(id);
		task.setTitle(title);
		task.setDescription(description);
		task.setPriority(priority);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		
	}
	
	public AddCommand(Long id, String title, String description, String priority, 
			String status, String label, Date startDate, Date endDate) {

		task = new Item();
		isUndo = true;
		assertNotNull(title);
		
		task.setId(id);
		task.setTitle(title);
		task.setDescription(description);
		task.setPriority(priority);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		
	}
	
	private void storeTask() {
		POMPOM.getStorage().getTaskList().add(task);
	}
	
	private Command createCounterAction() {
		DelCommand counterAction = new DelCommand(task.getId(), true);
		return counterAction;
	}
	
	private void updateUndoStack() {
		Command counterAction = createCounterAction();
		POMPOM.getUndoStack().push(counterAction);
	}
		
	public String execute() {
		returnMsg = MESSAGE_TASK_ADDED;
		if (!isUndo) updateUndoStack();
		storeTask();
		return returnMsg;
	}
	
}