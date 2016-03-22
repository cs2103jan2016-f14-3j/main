package command;

import java.util.ArrayList;

public class MultiDelCommand extends Command {

	ArrayList<DelCommand> deleteList;
	
	public MultiDelCommand(ArrayList<DelCommand> deleteList) {
		this.deleteList = deleteList;
	}
	
	public String execute() {
		for (int i = 0; i < deleteList.size(); i++) {
			deleteList.get(i).execute();
		}
		
		return null;
	}
	
}