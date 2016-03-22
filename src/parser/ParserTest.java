package parser;

import command.AddCommand;
import static org.junit.Assert.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Test;

public class ParserTest{

	Parser parser = new Parser();
	
	//@Test

	@Test
	public void testAddParser() {
		//edit 1 status asd
		//add go club
		//add do homework next week
		//add do cs2103:finish v0.1 p:high l:hw s:open f:next monday next tuesday
		//edit 1 title asd
		//delete 1
		//exit
		String userCommand = "lol lol lol lol lol 01-01-1999/00:00 01/02/1999/00:00";
		Date parsedStartDate =null;
	 	Date parsedEndDate = null;
		AddParser ap = new AddParser(userCommand);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy/HH:mm");
		String startDate = "01-01-1999/00:00";
		String endDate = "01-02-1999/00:00";
		try{			
			parsedStartDate = dateFormatter.parse(startDate);
		    parsedEndDate = dateFormatter.parse(endDate);
		} catch (ParseException e){
			
			System.out.println("err!");
			
		}
	
		AddCommand ac = new AddCommand("lol","lol","lol","lol","lol",parsedStartDate,parsedEndDate);
		assertEquals(ap.executeCommand(), ac);
	}
	
	@Test
	public void testAddCommand(){
		String userCommand = "add lol lol lol lol lol 01-01-1999/00:00 01-02-1999/00:00";
		AddCommand ac1 = (AddCommand)parser.executeCommand(userCommand);
		assertEquals(ac1,"lol");
	}
	


}
