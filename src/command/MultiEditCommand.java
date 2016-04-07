package command;
/**
 * @@author wen hao
 *
 */
import java.util.ArrayList;

public class MultiEditCommand extends Command {

	private static final String MESSAGE_MULTIEDIT = "Multiple fields has been edited";
	
	ArrayList<EditCommand> editList;
	
	public MultiEditCommand(ArrayList<EditCommand> editList) {
		this.editList = editList;
	}
	
	public String execute() {
		for (int i = 0; i < editList.size(); i++) {
			editList.get(i).execute();
		}
		
		returnMsg = MESSAGE_MULTIEDIT;
		return returnMsg;
	}
	
}