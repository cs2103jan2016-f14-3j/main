package command;

import java.util.ArrayList;

public class AddRecurringCommand extends Command {

	private static final String MESSAGE_RECURRING = "A recurring task has been added";
	
	ArrayList<AddCommand> addList;
	
	public AddRecurringCommand(ArrayList<AddCommand> addList) {
		System.out.println(addList.size());
		this.addList = addList;
	}

	
	public String execute() {
		for (int i = 0; i < addList.size(); i++) {
			addList.get(i).execute();
		}
		
		returnMsg = MESSAGE_RECURRING;
		return returnMsg;
	}
	
}