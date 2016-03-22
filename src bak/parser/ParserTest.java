package parser;

import command.AddCommand;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest{

	Parser parser = new Parser();
	

	//@Test
	public void testAddParser() {
		//edit 1 status asd
		//add go club
		//add do homework next week
		//add do cs2103:finish v0.1 p:high l:hw s:open f:next monday next tuesday
		//edit 1 title asd
		//delete 1
		//exit
	}
	@Test
	public void testPrettyTimeRecurring(){
		PrettyTimeParser timeParser = new PrettyTimeParser();
		List<DateGroup> dgl = timeParser.parseSyntax("every week");		
		assertTrue(dgl.get(0).isRecurring());
		System.out.println("ended");
	}
	
	@Test
	public void testPrettyTimeRecurringWithLimit(){
		PrettyTimeParser timeParser = new PrettyTimeParser();
		List<DateGroup> dgl = timeParser.parseSyntax("every monday until december");
		for (DateGroup dg: dgl){
			System.out.println(dg.getText());
		}		
		//assertTrue(dgl.get(0).isRecurring());
		assertEquals(6,dgl.get(0).getPosition());
		System.out.println(dgl.get(0).getText());
		System.out.println(dgl.get(0).getRecursUntil().toString());
		System.out.println("ended");
	}
	
	@Test
	public void testPrettyTime(){
		PrettyTimeParser timeParser = new PrettyTimeParser();
		List<DateGroup> dgl = timeParser.parseSyntax("16 mar");		
	//	assertTrue(dgl.get(0).getText());
		System.out.println(dgl.get(0).getText());
	}
	
}
	
	