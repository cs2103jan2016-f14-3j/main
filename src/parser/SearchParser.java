package parser;

import command.Command;
import command.DelCommand;
import command.SearchCommand;

/**
 * @@author William
 *
 */

public class SearchParser extends ArgsParser{
	String keyWord;
	public SearchParser(String commandArguments) {
		super(commandArguments);
		keyWord = commandArguments;
	}
	public Command executeCommand(){
		
			System.out.println(commandArgumentsString + "lol");
			return new SearchCommand(keyWord);
		
		
	}

}
