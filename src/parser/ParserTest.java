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
//		1. add <task>
//		2. add <task> <mmm dd>
//		3. add <task> <dd/mm/yyyy>
//		4. add <task> <f:mmm dd> <mmm dd>
//		5. add <task> <f:dd/mm/yyyy> <mmm dd>
//		6. add <task> <f:mmm dd> <dd/mm/yyyy>
//		7. add <task> <today/tomorrow/this week/month/year/ next week/month/year>
//		8. add <task> <today/tomorrow/this week/month/year/ next week/month/year> <f:today/tomorrow/this week/month/year/ next week/month/year>
//		9. add <task> <dd/mm/yyyy> <f:today/tomorrow/this week/month/year/ next week/month/year>
//		10. add <task> <dd mmm> <f:today/tomorrow/this week/month/year/ next week/month/year>
//		11. add <task> <today/tomorrow/this week/month/year/ next week/month/year><f:dd mmm>
//		12.add <task> <today/tomorrow/this week/month/year/ next week/month/year><f:dd/mm/yyyy>
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
}
	
	