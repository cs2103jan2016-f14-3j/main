package command;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import main.POMPOM;
import utils.Item;

/**
 * @@author wen hao
 *
 */
public class AddCommand extends Command {
	
	private static final String MESSAGE_TASK_ADDED = "%s added";	
	private Item task;
	private boolean isUndo;
	
	public AddCommand(String type, String title, String description, String priority, 
			String status, String label, Date startDate, Date endDate) {

		task = new Item();
		isUndo = false;
		assertNotNull(title);
		
		Long id = POMPOM.getStorage().getIdCounter();
		task.setId(id);
		task.setType(type);
		task.setTitle(title);
		task.setDescription(description);
		task.setPriority(priority);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		
		logger.log(Level.INFO, "AddCommand initialized");
	}
	
	public AddCommand(Long id, String type, String title, String description, String priority, 
			String status, String label, Date startDate, Date endDate) {

		task = new Item();
		isUndo = true;
		assertNotNull(title);
		
		task.setId(id);
		task.setType(type);
		task.setTitle(title);
		task.setDescription(description);
		task.setPriority(priority);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		
		logger.log(Level.INFO, "Counter action AddCommand initialized");
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
		
		if (!isUndo) updateUndoStack();
		storeTask();
		
		if (task.getType().equals(POMPOM.LABEL_EVENT)) {
			returnMsg = String.format(MESSAGE_TASK_ADDED, POMPOM.LABEL_EVENT);
			POMPOM.setCurrentTab(POMPOM.LABEL_EVENT);
		} else {
			returnMsg = String.format(MESSAGE_TASK_ADDED, POMPOM.LABEL_TASK);
			POMPOM.setCurrentTab(POMPOM.LABEL_TASK);
		}
		
		return returnMsg;
	}
	
}