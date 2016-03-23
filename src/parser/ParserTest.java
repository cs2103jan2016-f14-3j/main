package parser;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;
import main.POMPOM;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ParserTest{

	Parser parser = Parser.getInstance();
	PrettyTimeParser timeParser = new PrettyTimeParser();
	
	/*
	 * Tests if can add floating tasks
	 */
	@Test
	public void testAddCommand1(){
		AddParser add = new AddParser("do project");
		assertEquals("do project",add.getTitle());
	}
	/*
	 * Tests if can add tasks with end date
	 */
	@Test
	public void testAddCommand2(){
		AddParser add = new AddParser("do project 28 march");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * Tests if can add tasks with start and end date.
	 */
	@Test
	public void testAddCommand3(){
		AddParser add = new AddParser("do project e:28 march f:16 march");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		Date startDate= timeParser.parseSyntax("16 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
		assertEquals(startDate.compareTo(add.getStartDate()),1);	
	}
	
	/*
	 * Tests if can switch the order of the title and end date
	 */
	@Test
	public void testAddCommand4(){
		AddParser add = new AddParser("28 march do project");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * Tests if can switch the order of the start date, end date and title. 
	 */
	@Test
	public void testAddCommand5(){
		AddParser add = new AddParser("e:28 march f:16 march do project");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * Tests if can switch the order of the start date, end date and title.
	 */
	@Test
	public void testAddCommand6(){
		AddParser add = new AddParser("f:16 march e:28 march do project");
		assertEquals("do project",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * Tests if can add in recurring tasks with end date
	 */
	
	@Test
	public void testAddCommand7(){
		AddParser add = new AddParser("do cs2103 every friday e:28 march");
		assertEquals("do cs2103",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
	}
	
	/*
	 * tests if can switch the order of the fields for adding recurring tasks with
	 * end date.
	 */
	@Test
	public void testAddCommand8(){
		AddParser add = new AddParser("do cs2103 e:28 march every friday");
		assertEquals("do cs2103",add.getTitle());
		Date endDate= timeParser.parseSyntax("28 march").get(0).getDates().get(0);
		assertEquals(endDate.compareTo(add.getEndDate()),1);	
		assertTrue(add.getIsRecurring());
	}	
	
	/*
	 * Tests if can add in a task with only a parsable title 
	 */
	@Test
	public void testAddCommand9(){
		AddParser add = new AddParser("2103");
		assertEquals("2103",add.getTitle());
		Date endDate= new Date();
		assertEquals(endDate.compareTo(add.getEndDate()),0);
	}	
}
	
	