package storage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import utils.Task;



public class Storage {	

	private static final String DEFAULT_FILE_DIRECTORY = "PomPom";
	private static final String DEFAULT_FILE_NAME = "Storage.txt";
	private static final String DEFAULT_FILE_PATH = DEFAULT_FILE_DIRECTORY
			+ "/" + DEFAULT_FILE_NAME;
	
	private static final String SETTINGS_FILE_NAME = "settings.properties";
	private static final String SETTINGS_FILE_PATH = DEFAULT_FILE_DIRECTORY
			+ "/" + SETTINGS_FILE_NAME;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEE MMM d HH:mm:ss z yyyy");
	private static File newFolder = new File(DEFAULT_FILE_DIRECTORY);
	private static File file = new File(DEFAULT_FILE_PATH);
	//private static Logger logger = Logger.getLogger("Storage");

	private String storedFilePath;
	private String folderPath;
	private ArrayList<Task> taskList;
	private ArrayList<Task> doneTaskList;
	private ArrayList<Task> undoneTaskList;
	
	public Storage() {
		// TODO Auto-generated constructor stub
	}
	
}
