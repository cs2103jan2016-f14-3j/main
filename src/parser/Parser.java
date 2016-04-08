package parser;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;
import command.Command;
import command.PathCommand;
import main.POMPOM;
/**
 *  @@author Josh
 *
 */
public class Parser{
	
	/** List of Command types */
	private static final String CMD_ADD = "add";
	private static final String CMD_DELETE = "delete";
	private static final String CMD_DONE = "done";
	private static final String CMD_EDIT = "edit";
	private static final String CMD_EXIT = "exit";
	private static final String CMD_SEARCH = "search";
	private static final String CMD_SHOW = "show";
	private static final String CMD_UNDO ="undo";
	private static final String CMD_PATH = "setpath";
	private static final String CMD_EVENT = "event";
	private static final String CMD_HELP_1 = "help";
	private static final String CMD_HELP_2 = "?";
	private static final String CMD_DELETE_RECUR_1 = "delete recur";
	private static final String CMD_EDIT_RECUR_1= "edit recur";
	private static final String CMD_DELETE_RECUR_2 = "delete r";
	private static final String CMD_EDIT_RECUR_2 = "edit r";
	
	private static final String[] CMD_ARRAY = {CMD_ADD, CMD_DELETE, CMD_DONE,
												CMD_EDIT, CMD_EXIT, CMD_SEARCH,
												CMD_SHOW, CMD_UNDO, CMD_PATH,
												CMD_EVENT, CMD_HELP_1, CMD_HELP_2, CMD_DELETE_RECUR_1,
												CMD_EDIT_RECUR_1, CMD_DELETE_RECUR_2,
												CMD_EDIT_RECUR_2};
	

	private static final String CMD_VIEW = "view";	
	
	private static final int COMMAND_ARRAY_SIZE = 2; 
	private static final int COMMAND_TYPE_INDEX = 0;
	private static final int COMMAND_ARGUMENT_INDEX = 1;
	
	private static Parser parserInstance;
		
	private static Logger logger = Logger.getLogger("Parser");
	
	public static Parser getInstance()
	{
		if (parserInstance == null)
			parserInstance = new Parser();

		return parserInstance;
	}
	
	private Parser(){

	}
	
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
				AddParser addTaskArgumentParser = new AddParser(commandArgument, POMPOM.LABEL_TASK);
				return addTaskArgumentParser.getCommand();
			case CMD_EVENT:
				AddParser addEventArgumentParser = new AddParser(commandArgument, POMPOM.LABEL_EVENT);
				return addEventArgumentParser.getCommand();
			case CMD_DELETE:
				DeleteParser deleteArgumentParser = new DeleteParser(commandArgument);
				return deleteArgumentParser.executeCommand();
			case CMD_EDIT:
				EditParser EditArgumentParser = new EditParser(commandArgument);
				return EditArgumentParser.executeCommand();
			case CMD_SEARCH:
				SearchParser searchParser = new SearchParser(commandArgument);
				return searchParser.executeCommand();
			case CMD_SHOW:
				//return new ShowParser(commandArgument);
				//Hard coded
			case CMD_EXIT:
				ExitParser exitParser = new ExitParser();
				//return exitParser.executeCommand();
				System.exit(0);
			case CMD_UNDO:
				UndoParser undoParser = new UndoParser();
				return undoParser.executeCommand();
			case CMD_HELP_1:
			case CMD_HELP_2:
				HelpParser helpParser = new HelpParser();
				return helpParser.executeCommand();
			case CMD_PATH:
				return new PathCommand(commandArgument);
			case CMD_DELETE_RECUR_1:
			case CMD_DELETE_RECUR_2:
				return new command.DelRecurringCommand(Long.parseLong(commandArgument));
			case CMD_EDIT_RECUR_1:
			case CMD_EDIT_RECUR_2:
				EditRecurringParser EditRecurringArgumentParser = new EditRecurringParser(commandArgument);
				return EditRecurringArgumentParser.executeCommand();
			case CMD_VIEW:
				ViewParser viewParser = new ViewParser(commandArgument);
				return viewParser.executeCommand();
			case CMD_DONE:
				DoneParser DoneArgumentParser = new DoneParser(commandArgument);
				return DoneArgumentParser.executeCommand();
	}	
		InvalidParser InvalidArgumentParser = new InvalidParser(userCommand);
		return InvalidArgumentParser.executeCommand();

	}
	
	private String[] splitCommand(String userCommand){
		String[] outputArray = new String[COMMAND_ARRAY_SIZE];
		for (String command: CMD_ARRAY){
			if (userCommand.indexOf(command)==0){
				System.out.println(userCommand);
				outputArray[COMMAND_TYPE_INDEX]=command.trim();
				outputArray[COMMAND_ARGUMENT_INDEX]=userCommand.substring(command.length()).trim();
			}
		}
		return outputArray;
	}

}
 	