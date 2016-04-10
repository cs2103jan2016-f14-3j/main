package parser;

import command.Command;
import command.DelCommand;
import command.InvalidCommand;
import command.SearchCommand;
/**
 *  @@author A0121760R
 *
 */
public class SearchParser extends ArgsParser{
	String keyWord;
	public SearchParser(String commandArguments) {
		super(commandArguments);
		keyWord = commandArguments;
	}
	public Command parse(){
			if(keyWord == null || keyWord.equals("")){
				return new InvalidCommand("Search must have arguments");
			}

			return new SearchCommand(keyWord);
		
		
	}

}
