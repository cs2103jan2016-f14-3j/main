package parser;

import java.io.IOException;

public class ParserMain {
	
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format : %1$s";

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
		//User inputs nothing
		if (isEmptyCommand(userCommand)){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		String commandTypeString = getCommandType(userCommand);
		String commandArguments = getArguments(userCommand);
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);		
		
		return "";
	}
	
	public String getCommandType(String userCommand){
		return "";
	}
	
	public String getArguments(String userCommand){
		return "";
	}
	
	public COMMAND_TYPE determineCommandType(String commandTypeString){
		return COMMAND_TYPE.ADD_TASK;
	}
	
	public boolean isEmptyCommand(String userCommand){
		return userCommand.trim().equals("");
	}
}
