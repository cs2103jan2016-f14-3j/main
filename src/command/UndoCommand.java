package command;
import java.util.logging.Level;

/**
 * @@author wen hao
 *
 */
import main.POMPOM;

public class UndoCommand extends Command{
	
	private static final String MESSAGE_UNDO = "Previous action was successfully undid";
	
	public String execute() {
		Command undo = (Command) POMPOM.getUndoStack().pop();
		undo.execute();
		logger.log(Level.INFO, "UndoCommand has be executed");
		returnMsg = MESSAGE_UNDO;
		return returnMsg;
	}

}
