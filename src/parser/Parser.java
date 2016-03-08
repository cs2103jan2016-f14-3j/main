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
	
	
	private static final int COMMAND_ARRAY_SIZE = 2; 
	private static final int COMMAND_TYPE_INDEX = 0;
	private static final int COMMAND_ARGUMENT_INDEX = 1;
	
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
	public Command executeCommand(String userCommand){	
		
		
		String[] parsedCommandArray = splitCommand(userCommand);
		String commandType = parsedCommandArray[COMMAND_TYPE_INDEX];
		String commandArgument = parsedCommandArray[COMMAND_ARGUMENT_INDEX];
		
		switch (commandType){
			case CMD_ADD:
				return new AddParser(commandArgument);
			case CMD_DELETE:
				return new DeleteParser(commandArgument);
			case CMD_EDIT:
				return new EditParser(commandArgument);
			case CMD_SEARCH:
				return new SearchParser(commandArgument);
			case CMD_SHOW:
				return new ShowParser(commandArgument);
			case CMD_EXIT:
				return new ExitParser();
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

}
