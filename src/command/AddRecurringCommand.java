package command;

import java.util.ArrayList;
import java.util.logging.Level;

import org.ocpsoft.prettytime.shade.org.apache.commons.lang.ObjectUtils.Null;

import main.POMPOM;
import utils.Item;

/**
 * @@author wen hao
 *
 */
public class AddRecurringCommand extends Command {

	private static final String MESSAGE_RECURRING = "Recurring tasks has been added";

	ArrayList<AddCommand> addList;
	ArrayList<Item> itemList; 
	ArrayList<Item> taskList = getTaskList();
	Long firstId = null, prevId = null, currentId, nextId = null;
	Long[] idList;
	boolean isUndo;

	public AddRecurringCommand(ArrayList<AddCommand> addList) {
		this.addList = addList;
		this.idList = new Long[addList.size()];
		this.isUndo = false;
		
		logger.log(Level.INFO, "AddRecurringCommand initialized");
	}
	
	public AddRecurringCommand(ArrayList<Item> itemList, boolean isUndo) {
		this.itemList = itemList;
		this.isUndo = true;
		
		logger.log(Level.INFO, "Counter AddRecurringCommand initialized");
	}
	
	private Command createCounterAction() {
		return new DelRecurringCommand(idList);
	}
	
	private void updateUndoStack() {
		Command counterAction = createCounterAction();
		POMPOM.getUndoStack().push(counterAction);
	}

	public String execute() {

		if (isUndo) {
			
			for (int i = 0; i < itemList.size(); i++) {
				taskList.add(itemList.get(i));
			}
			
			logger.log(Level.INFO, "AddRecurringCommand has been executed");
			returnMsg = MESSAGE_RECURRING;
			return returnMsg;
			
		} else {
			
			for (int i = 0; i < addList.size(); i++) {

				addList.get(i).execute();
				Item currentTask = taskList.get(taskList.size() - 1);
				currentId = currentTask.getId();
				
				currentTask.setRecurring(true);
				currentTask.setPrevId(prevId);
				prevId = currentId;
				idList[i] = currentId;

				if (i == 0) {
					firstId = currentId;
				}
				
				if (i == addList.size() - 1) {
					nextId = firstId;
				} else {
					nextId = currentId + 1;
				}
				
				currentTask.setNextId(nextId);
				System.out.println("CURRENT ID: " + currentId + ", NEXT ID: " + nextId);
			}
			
			updateUndoStack();
			logger.log(Level.INFO, "AddRecurringCommand has been executed");
			returnMsg = MESSAGE_RECURRING;
			return returnMsg;
		}

	}

}