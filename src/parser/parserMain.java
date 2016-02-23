package parser;

import java.io.IOException;

public class parserMain {

	enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, EDIT_TASK, SEARCH_TASK, DISPLAY_TASK, 
		INVALID, EXIT
	};
	
	/**
	 * This operation takes in the command specified by the
	 * user, executes it and returns a message about
	 * the execution information to the user.
	 * 
	 * @param userCommand
	 * 			is the command the user has given to the
	 * 			program
	 * @return
	 * 		the message containing information about the
	 * 		execution of the command. 
	 */
	public String executeCommand(String userCommand){	
		return "";
	}
}
