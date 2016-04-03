package command;

import java.util.ArrayList;
import java.util.logging.Level;
/**
 * @@author wen hao
 *
 */
public class AddRecurringCommand extends Command {

	private static final String MESSAGE_RECURRING = "A recurring task has been added";
	
	ArrayList<AddCommand> addList;
	
	public AddRecurringCommand(ArrayList<AddCommand> addList) {
		this.addList = addList;
	}
	
	public String execute() {
		for (int i = 0; i < addList.size(); i++) {
			addList.get(i).execute();
		}
		
		logger.log(Level.INFO, "AddRecurringCommand has be executed");
		returnMsg = MESSAGE_RECURRING;
		return returnMsg;
	}
	
}