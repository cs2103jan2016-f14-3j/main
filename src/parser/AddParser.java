package parser;

import java.util.Date;
import java.util.List;

import command.Command;
import command.InvalidCommand;
import command.AddCommand;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;


public class AddParser extends ArgsParser{
	
	/*
	 * Possible add commands:
	 * add title = adds title without any extra stuff
	 * add title date = add title + date
	 * add title:desc date = add title + desc + date
	 * add title:desc p:high date = add title + description + priority + date
	 * add title:desc p:high l:label date = add title + desc + priority + label + date
	 * add title:desc p:high l:label s:startdate date = add title desc priority + label + startdate + enddate 
	 */
	
	private String itemTitle= null;
	private String itemDescription = null;
	private String itemPriority = null;
	private String itemStatus = null;
	private String itemLabel = null;
	private Date itemStartDate = null;
	private Date itemEndDate = null;
	
	private PrettyTimeParser timeParser = new PrettyTimeParser();
	
	public AddParser(String userCommand){
		super(userCommand);	
		if (this.noArgs){
			invalidArgs();
			return;
		} else {		
			extractStartDate();
			extractEndDate();
			extractPriority();
			extractStatus();
			extractLabel();
			extractDescription();
			itemTitle = commandArgumentsString.trim();
		}
	}
	
	public Command executeCommand(){
		if (itemTitle==""){
			return new InvalidCommand(commandArgumentsString);
		}
		if (itemDescription==null && itemPriority==null && itemStatus==null && itemLabel==null && itemStartDate==null && itemEndDate==null){
			return new AddCommand(itemTitle);
		} else if (itemDescription==null && itemPriority==null && itemStatus==null && itemLabel==null && itemStartDate==null){
			return new AddCommand(itemTitle, itemEndDate);
		} else{
			return new AddCommand(itemTitle, itemDescription, itemPriority, itemStatus, itemLabel, itemStartDate, itemEndDate);
		}
	}
	
	private void extractDescription(){
		int index = commandArgumentsString.indexOf(":");
		if (index>0){
			itemDescription = commandArgumentsString.substring(index+1);
			commandArgumentsString = commandArgumentsString.substring(0, index);
		}
	}
	
	public void extractPriority(){
		int index = commandArgumentsString.indexOf("p:");
		int indexSpace = commandArgumentsString.indexOf(" ",index);
		if (index>0){
			itemPriority = commandArgumentsString.substring(index+2, indexSpace);
			commandArgumentsString = commandArgumentsString.substring(0, index)+commandArgumentsString.substring(indexSpace);
		}

	}
	
	public void extractStatus(){
		int index = commandArgumentsString.indexOf("s:");
		int indexSpace = commandArgumentsString.indexOf(" ",index);
		if (index>0){
			itemStatus = commandArgumentsString.substring(index+2, indexSpace);
			commandArgumentsString = commandArgumentsString.substring(0, index)+commandArgumentsString.substring(indexSpace);
		}
	}
	
	public void extractLabel(){
		int index = commandArgumentsString.indexOf("l:");
		int indexSpace = commandArgumentsString.indexOf(" ",index);
		if (index>0){
			itemLabel = commandArgumentsString.substring(index+2, indexSpace);
			commandArgumentsString = commandArgumentsString.substring(0, index)+commandArgumentsString.substring(indexSpace);
		}
	}
	
	public void extractStartDate(){
		
		int index = commandArgumentsString.indexOf("f:"); //from
		if (index>0){
			itemLabel = commandArgumentsString.substring(index);
			List<Date> startDateGroup = timeParser.parseSyntax(itemLabel).get(0).getDates();
			String startDateString = timeParser.parseSyntax(itemLabel).get(0).getText();
			
			itemStartDate = startDateGroup.get(0);
			commandArgumentsString = commandArgumentsString.replace(startDateString, " ");
		} else{
			itemStartDate = new Date();
		}
	}
	
	public void extractEndDate(){ 
		List<DateGroup> endDateGroup = timeParser.parseSyntax(commandArgumentsString);
		if (endDateGroup.size()>0){
			itemEndDate=endDateGroup.get(0).getDates().get(0);
			String endDateString = endDateGroup.get(0).getText();
			commandArgumentsString = commandArgumentsString.replace(endDateString, " ");
		}
	}
	
}