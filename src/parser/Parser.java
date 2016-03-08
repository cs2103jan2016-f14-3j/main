<<<<<<< HEAD
package parser;

public class Parser{
	
	/** List of Command types */
	private static final String CMD_ADD = "add";
	private static final String CMD_DELETE = "delete";
	private static final String CMD_DONE = "done";
	private static final String CMD_EDIT = "edit";
	private static final String CMD_EXIT = "exit";
	private static final String CMD_SEARCH = "search";
	private static final String CMD_SHOW = "show";
	
=======
//Parser

package parser;

import java.io.IOException;
public class Parser{
	
>>>>>>> 3f4cc92b18dbb791fd6ac42bd7a1435d60fac7e1
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format : %1$s";
	private static final String MESSAGE_ARGUMENTS_NEEDED = "Arguments needed!";
	private static final String MESSAGE_PROGRAM_ERROR = "Program has encountered an error";
	private static final String MESSAGE_ADDED_TEXT = "Added '%1$s' to task list!";
	
<<<<<<< HEAD
	private static final int COMMAND_ARRAY_SIZE = 2; 
	private static final int COMMAND_TYPE_INDEX = 0;
	private static final int COMMAND_ARGUMENT_INDEX = 1;
=======
	
	private static final int NEGATIVE_INDEX = -1;
	
	enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, EDIT_TASK, SEARCH_TASK, DISPLAY_TASK, 
		INVALID, EXIT
	};
>>>>>>> 3f4cc92b18dbb791fd6ac42bd7a1435d60fac7e1
	
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
<<<<<<< HEAD
	public Command executeCommand(String userCommand){	
		
		
		String[] parsedCommandArray = splitCommand(userCommand);
		String commandType = parsedCommandArray[COMMAND_TYPE_INDEX];
		String commandArgument = parsedCommandArray[COMMAND_ARGUMENT_INDEX];
		
		switch (commandType){
			case CMD_ADD:
				return AddParser(commandArgument);
			case CMD_DELETE:
				return deleteParser(commandArgument);
			case CMD_EDIT:
				return editParser(commandArgument);
			case CMD_SEARCH:
				return searchParser(commandArgument);
			case CMD_SHOW:
				return showParser(commandArgument);
			case CMD_EXIT:
				//return exitParser();
	}
		return InvalidParser(userCommand);
	}
	
	public String[] splitCommand(String userCommand){
		String[] outputArray = new String[COMMAND_ARRAY_SIZE];
		outputArray[COMMAND_TYPE_INDEX] = getCommandType(userCommand);
		outputArray[COMMAND_ARGUMENT_INDEX] = getArguments(userCommand, outputArray[0]);
		return outputArray;
	}
	
	public String getCommandType(String userCommand){
		String[] toSplit = userCommand.split(" ", COMMAND_ARRAY_SIZE);
		return toSplit[COMMAND_TYPE_INDEX].toLowerCase().trim();
	}
	
	public String getArguments(String userCommand, String commandType) {
		String[] toSplit = userCommand.split(" ", COMMAND_ARRAY_SIZE);
		return toSplit[COMMAND_ARGUMENT_INDEX].toLowerCase().trim();
	}

=======
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
				return String.format(MESSAGE_ADDED_TEXT, commandArguments); 
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
		return MESSAGE_PROGRAM_ERROR;
	}
	
	public String getCommandType(String userCommand){
		int firstSpaceIndex = userCommand.indexOf(" ");
		
		if (hasNoSpaceInCommand(firstSpaceIndex)){
			return userCommand;
		}
		else{
			return userCommand.substring(0,firstSpaceIndex);
		}
	}
	
	public String getArguments(String userCommand) {
		return userCommand.replace(getCommandType(userCommand), "").trim();
	}
	
	public COMMAND_TYPE determineCommandType(String commandTypeString){
		
		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD_TASK;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
		 	return COMMAND_TYPE.DELETE_TASK;
		} else if (commandTypeString.equalsIgnoreCase("edit")) {
		 	return COMMAND_TYPE.EDIT_TASK;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
		 	return COMMAND_TYPE.SEARCH_TASK;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY_TASK;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
		 	return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
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
	
	public boolean hasNoSpaceInCommand(int firstSpaceIndex) {
		return firstSpaceIndex == NEGATIVE_INDEX;
	}
>>>>>>> 3f4cc92b18dbb791fd6ac42bd7a1435d60fac7e1
}
