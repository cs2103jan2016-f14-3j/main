package command;

import java.io.IOException;

import main.POMPOM;

public class PathCommand extends Command {
	String storageFilePath;
	
	public PathCommand(String storageFilePath) {
		this.storageFilePath = storageFilePath;
	}
	
	@Override
	public String execute() {
		
		POMPOM.getStorage().setStorageFilePath(storageFilePath);
		try {
			POMPOM.getStorage().saveSettings();
			POMPOM.getStorage().init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "setted";
	}
	
}
