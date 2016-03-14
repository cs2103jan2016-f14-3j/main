package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {
	public static void writeStringToFile(File file,String text) throws IOException{
		FileWriter out = new FileWriter(file);
		out.write(text);
		out.close();
	}
	
	public static String getStringFromFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
}
