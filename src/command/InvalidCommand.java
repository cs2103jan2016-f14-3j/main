package command;
/**
 * @@author wen hao
 *
 */
public class InvalidCommand extends Command{

	private String MESSAGE_ERROR = "%s is not a valid command.";
	
	public InvalidCommand(String error) {
		this.MESSAGE_ERROR = error;
	}
	
	public String execute() {
		String returnMsg = MESSAGE_ERROR;
		return returnMsg;
	}

	
	
}
