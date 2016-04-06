package command;
/**
 * @@author wen hao
 *
 */
public class InvalidCommand extends Command{

	private String MESSAGE_ERROR = "%s is not a valid command";
	private String error;
	
	public InvalidCommand(String error) {
		this.error = error;
	}
	
	public String execute() {
		returnMsg = String.format(MESSAGE_ERROR, error);
		return returnMsg;
	}

	
	
}
