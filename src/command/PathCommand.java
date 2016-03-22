package command;

import java.io.IOException;

import main.POMPOM;

public class PathCommand extends Command {
	
	private static final String MESSAGE_SET_PATH = "Storage path set to: %s";
	
	private String storageFilePath;
	
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
		
		returnMsg = String.format(MESSAGE_SET_PATH, storageFilePath);
		return returnMsg;
	}
	
}
