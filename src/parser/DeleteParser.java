package parser;

import command.Command;
import command.DelCommand;
import command.UndoCommand;

import java.text.SimpleDateFormat;


public class DeleteParser extends ArgsParser{
	
	private int itemID;
	private Command outputCommand = null;
	
	public DeleteParser(String userCommand){
		super(userCommand);
		getItemId();
	}
	
	public Command executeCommand(){
		if (outputCommand == null){
			System.out.println(commandArgumentsString + "lol");
			return new DelCommand(itemID);
		} else{
			return outputCommand;
		}
		
	}
	
	public void getItemId(){
		try{
			itemID = Integer.parseInt(commandArgumentsString);
		} catch (Exception e){
			InvalidParser InvalidArgumentParser = new InvalidParser(commandArgumentsString);
			outputCommand = InvalidArgumentParser.executeCommand();
		}
	}	
}
