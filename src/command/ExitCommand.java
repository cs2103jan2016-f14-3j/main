package command;

import java.io.IOException;

import main.POMPOM;
/**
 * @@author wen hao
 *
 */
public class ExitCommand extends Command{
	
	public ExitCommand() {
		try {
			POMPOM.getStorage().saveStorage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String execute() {
		System.exit(0);
		return returnMsg;
	}
}
