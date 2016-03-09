package parser;

public class ArgsParser {
	protected String[] argsArray;
	public ArgsParser(String commandArguments){
		argsArray = commandArguments.split(" ");		
	}
}
