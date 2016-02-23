package parser;

import static org.junit.Assert.*;
import org.junit.Test;
import parser.Parser.COMMAND_TYPE;

public class ParserTest {

	Parser parser = new Parser();
	
	@Test
	public void testExecuteCommandEmptyCommand() {
		String result = parser.executeCommand("");
		assertEquals(result,"invalid command format : ");
	}
	
	@Test
	public void testDetermineCommandType(){
		COMMAND_TYPE actual = parser.determineCommandType("add");
		assertEquals(actual,COMMAND_TYPE.ADD_TASK);
	}
	
	@Test
	public void testGetCommandType(){
		String actual = parser.getCommandType("add 1");
		assertEquals(actual, "add");
	}
	
	@Test
	public void testGetArguments(){
		String actual = parser.getArguments("add 1");
		assertEquals(actual, "1");
	}
	
	//@Test
	public void testExecuteCommandEmptyArgument(){
		String result = parser.executeCommand("add 1");
		assertEquals(result, "Arguments needed!");
	}

}
