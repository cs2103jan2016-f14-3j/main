package main;

import java.io.IOException;

import storage.Storage;

public class POMPOM {

	private static Storage storage; 
	
	public static void main(String[] args) {
		
		try {
			storage = new Storage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Storage getStorage() {
		return storage;
	}
	
	
	
}