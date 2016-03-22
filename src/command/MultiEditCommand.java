package command;

import java.util.ArrayList;

public class MultiEditCommand extends Command {

	private static final String MESSAGE_MULTIEDIT = "Multiple fields of task %s has been edited";
	
	ArrayList<EditCommand> editList;
	
	public MultiEditCommand(ArrayList<EditCommand> editList) {
		this.editList = editList;
	}
	
	public String execute() {
		for (int i = 0; i < editList.size(); i++) {
			editList.get(i).execute();
		}
		
		returnMsg = String.format(MESSAGE_MULTIEDIT, editList.size());
		return returnMsg;
	}
	
}