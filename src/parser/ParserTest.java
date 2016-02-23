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
	public void testExecuteCommandEmptyArgument(){
		String result = parser.executeCommand("add");
		assertEquals(result, "Arguments needed!");
	}

}
