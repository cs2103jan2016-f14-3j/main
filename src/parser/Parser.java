package parser;

import java.io.IOException;

import TextBuddy.COMMAND_TYPE;

public class Parser{
	
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format : %1$s";
	private static final String MESSAGE_ARGUMENTS_NEEDED = "Arguments needed!";

	
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
		if (hasEmptyCommand(userCommand)){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		String commandTypeString = getCommandType(userCommand);
		String commandArguments = getArguments(userCommand);
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);		
		
		if (hasEmptyArgument(commandArguments, commandType)){
			return MESSAGE_ARGUMENTS_NEEDED;
		}	
	
		switch (commandType){
			case ADD_TASK:
				//addTask();
			case DELETE_TASK:
				//deleteTask();
			case EDIT_TASK:
				//editTask();
			case SEARCH_TASK:
				//editTask();
			case DISPLAY_TASK:
				//displayTask();
			case INVALID:
				return String.format(MESSAGE_INVALID_FORMAT, userCommand);
			case EXIT:
				//exitPomPom();
	}
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
	
	private boolean hasEmptyArgument(String commandArguments, COMMAND_TYPE commandType) {
		return ((commandType==COMMAND_TYPE.ADD_TASK || 
				commandType==COMMAND_TYPE.DELETE_TASK|| 
				commandType==COMMAND_TYPE.EDIT_TASK ||
				commandType==COMMAND_TYPE.SEARCH_TASK ||
				commandType==COMMAND_TYPE.DISPLAY_TASK)&& 
				commandArguments.equals(""));
	}
	
	public boolean hasEmptyCommand(String userCommand){
		return userCommand.trim().equals("");
	}
}
