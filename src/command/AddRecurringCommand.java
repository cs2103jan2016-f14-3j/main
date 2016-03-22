package command;

import java.util.ArrayList;

public class AddRecurringCommand extends Command {

	ArrayList<DelCommand> addList;
	
	public AddRecurringCommand(ArrayList<DelCommand> addList) {
		this.addList = addList;
	}
	
	public String execute() {
		for (int i = 0; i < addList.size(); i++) {
			addList.get(i).execute();
		}
		
		return null;
	}
	
}