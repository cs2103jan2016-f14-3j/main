package parser;

public class ArgsParser {
	private String[] argsArray;
	public ArgsParser(String commandArguments){
		argsArray = commandArguments.split(" ");		
	}
}
