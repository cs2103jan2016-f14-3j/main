package command;

public class InvalidCommand extends Command{

	private String error;
	
	public InvalidCommand(String error) {
		this.error = error;
	}
	
	public String execute() {
		String returnMsg = error;
		return returnMsg;
	}

	
	
}
