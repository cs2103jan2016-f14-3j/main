package parser;

import command.Command;
import command.DelCommand;
import command.InvalidCommand;
import command.SearchCommand;
/**
 *  @@author A0121760R
 */
public class SearchParser extends ArgsParser{
	private String keyWord;
	public SearchParser(String commandArguments) {
		super(commandArguments);
		keyWord = commandArguments;
	}
	
	public Command parse(){
			if(isInvalidKeyword()){
				return new InvalidCommand("Search must have arguments");
			}
			return new SearchCommand(keyWord);
	}
	
	private boolean isInvalidKeyword() {
		return keyWord == null || keyWord.equals("");
	}

}
