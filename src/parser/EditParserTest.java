package parser;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 *  @@author A0121760R
 *
 */
public class EditParserTest {

	@Test
	public void testGetFields() {
		EditParser editParser = new EditParser("1 title new title");
		assertEquals(editParser.getField(),"title");
	}
	

}
