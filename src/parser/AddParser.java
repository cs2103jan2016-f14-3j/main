package parser;

import java.util.Date;
import command.Command;
import command.AddCommand;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class AddParser extends ArgsParser{
	
	private final int INDEX_TITLE = 0;
	private final int INDEX_DESCRIPTION = 1;
	private final int INDEX_PRIORITY = 2;
	private final int INDEX_STATUS = 3;
	private final int INDEX_LABEL = 4;
	private final int INDEX_STARTDATE = 5;
	private final int INDEX_ENDDATE = 6;
	
	private String itemTitle;
	private String itemDescription;
	private String itemPriority;
	private String itemStatus;
	private String itemLabel;
	private Date itemStartDate;
	private Date itemEndDate;
	
	private SimpleDateFormat dateFormatter;
	
	public AddParser(String userCommand){
		super(userCommand);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MM yyyy HH:mm");
		itemTitle = getTitle();
		itemDescription = getDescription();
		itemPriority = getPriority();
		itemStatus = getStatus();
		itemLabel = getLabel();
		itemStartDate = getStartDate();
		itemEndDate = getEndDate();
	}
	
	public Command executeCommand(){
		return new AddCommand(itemTitle, itemDescription, itemPriority, itemStatus, itemLabel, itemStartDate, itemEndDate); 
	}
	
	public String getTitle(){
		return this.argsArray[INDEX_TITLE];
	}
	
	public String getDescription(){
		return this.argsArray[INDEX_DESCRIPTION];
	}
	
	public String getPriority(){
		return this.argsArray[INDEX_PRIORITY];
	}
	
	public String getStatus(){
		return this.argsArray[INDEX_STATUS];
	}
	
	public String getLabel(){
		return this.argsArray[INDEX_LABEL];
	}
	
	public Date getStartDate(){ 
		String dateString = this.argsArray[INDEX_STARTDATE];
		Date parsedDate = null;
		if (dateString==null){
			parsedDate = new Date();
		} else{
			try{
				parsedDate = dateFormatter.parse(dateString);
			} catch (ParseException e){
				//do something
			}
		}
		return parsedDate;
	}
	
	public Date getEndDate(){ 
		String dateString = this.argsArray[INDEX_ENDDATE];
		Date parsedDate = null;
		try{
			parsedDate = dateFormatter.parse(dateString);
		} catch (ParseException e){
			//do something
		}
		
		return parsedDate;
	}
	
}
