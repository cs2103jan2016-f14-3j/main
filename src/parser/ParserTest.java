package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

	Parser parser = new Parser();
	
	@Test
	public void testExecuteCommandEmptyCommand() {
		String result = parser.executeCommand("");
		assertEquals(result,"invalid command format : ");
	}
	
	@Test
	public void testExecuteCommandEmptyArgument() {
		String result = parser.executeCommand("add");
		assertEquals(result,"invalid command format : ");
	}


}
