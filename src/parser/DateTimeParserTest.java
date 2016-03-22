package parser;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import main.POMPOM;
import org.junit.Test;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

public class DateTimeParserTest {

	PrettyTimeParser timeParser = new PrettyTimeParser();
	
	//@Test
	public void testEveryWeek() {
		DateTimeParser dp = new DateTimeParser("end","shopping every monday");
		String output = dp.getString();
		assertEquals(output,"every monday");
	}
	
	//@Test
	public void testFromEveryWeek() {
		//make this invalid!
		DateTimeParser dp = new DateTimeParser("start","shopping f:every monday");
		String output = dp.getString();
		assertEquals(output,"f:every monday");
	}
	
	//@Test
	public void testFormat1() {
		DateTimeParser dp = new DateTimeParser("end","shopping e:28 march f:16 march");
		String output = dp.getString();
		assertEquals(output,"e:28 march");
	}
	
	//@Test
	public void testFormat2() {
		DateTimeParser dp = new DateTimeParser("start","shopping f:16 march e:28 march");
		String output = dp.getString();
		assertEquals(output,"f:16 march");
	}
	
	//@Test
	public void testFormat3() {
		DateTimeParser dp = new DateTimeParser("end","shopping 16/03/2016");
		String output = dp.getString();
		assertEquals(output,"16/03/2016");
	}
	
	@Test
	public void testFormat4() {
		DateTimeParser dp = new DateTimeParser("start","shopping f:16/03/2016 e:28/03/2016");
		String output = dp.getString();
		assertEquals(output,"f:16/03/2016");
	}
	
	//@Test
	public void testFormat5() {
		DateTimeParser dp = new DateTimeParser("start","shopping f:16 march e:28/03/2016");
		String output = dp.getString();
		assertEquals(output,"f:16 march");
	}
	
	//@Test
	public void testFormat6() {
		DateTimeParser dp = new DateTimeParser("end","shopping e:28/03/2016 f:16 march");
		String output = dp.getString();
		assertEquals(output,"e:28/03/2016");
	}
	
	//@Test
	public void testFormat7() {
		DateTimeParser dp = new DateTimeParser("end","shopping e:28/03/2016 f:16 mar");
		String output = dp.getString();
		assertEquals(output,"e:28/03/2016");
	}
	
	//@Test
	public void testFormat8() {
		DateTimeParser dp = new DateTimeParser("end","shopping 16 march");
		String output = dp.getString();
		assertEquals(output,"16 march");
	}
	
//	@Test
	public void testHasPrefix() {
		boolean output = DateTimeParser.hasPrefix("shopping 16 march");
		assertFalse(output);
	}
	
	//@Test
	public void testAddStart() {
		DateTimeParser dp = new DateTimeParser("end","shopping tomorrow");
		Date endDate = timeParser.parseSyntax("tomorrow").get(0).getDates().get(0);
		Date output = dp.getDate();
		assertEquals(endDate.toString().trim(),output.toString().trim());
	}
	
	//@Test
	public void testbreakUpStartAndEndDatesStart(){
		String output=DateTimeParser.breakUpStartAndEndDates("start","do project e:28 march f:16 march");
		assertEquals(output,"do project f:16 march");
	}
	
	//@Test
	public void testbreakUpStartAndEndDatesEnd(){
		String output=DateTimeParser.breakUpStartAndEndDates("end","do project e:28 march f:16 march");
		assertEquals(output,"e:28 march");
	}
//	@Test
	public void testParseMessedUpTitle() {
		DateTimeParser dp = new DateTimeParser("end","do cs1231231231231232132341 tomorrow");
		Date endDate = timeParser.parseSyntax("tomorrow").get(0).getDates().get(0);
		Date output = dp.getDate();
		assertEquals(endDate.toString().trim(),output.toString().trim());
	}
	
	//@Test
	public void testPrettyTimeParser(){
		PrettyTimeParser parser = new PrettyTimeParser();
		List<DateGroup> dg=parser.parseSyntax("every monday until 28 march");
		Date d= dg.get(0).getDates().get(0);
		System.out.println(d.toString());
		System.out.println(dg.get(0).getRecursUntil());
		System.out.println(dg.get(0).getText());
	}
	
//	@Test
	public void testEvery(){
		PrettyTimeParser parser = new PrettyTimeParser();
		List<DateGroup> dg=parser.parseSyntax("every man needs a penis every monday");

		System.out.println(dg.get(0).isRecurring());
		System.out.println(dg.get(0).getText());
	}
	
	@Test
	public void testAddNumberOnly() {
		DateTimeParser dp = new DateTimeParser("end","2103");
		System.out.println(dp.getString());
		System.out.println(dp.getDate().toString());
	}

}
