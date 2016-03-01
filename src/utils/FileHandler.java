package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {
	public static void writeStringToFile(String fileName,String text) throws FileNotFoundException{
		PrintWriter out = new PrintWriter(fileName);
		out.println(text);
		out.close();
	}
	
	public static String getStringFromFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
}
