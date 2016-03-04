package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.FileHandler;
import utils.Task;
import utils.TaskAdapter;
import utils.UserTaskList;

public class Storage {

	private static final String DEFAULT_FILE_DIRECTORY = "PomPom";
	private static final String DEFAULT_FILE_NAME = "Storage.txt";
	private static final String DEFAULT_STORAGE_FILE_PATH = DEFAULT_FILE_DIRECTORY
			+ "/" + DEFAULT_FILE_NAME;

	private static final String SETTINGS_FILE_NAME = "settings.properties";
	private static final String SETTINGS_FILE_PATH = DEFAULT_FILE_DIRECTORY
			+ "/" + SETTINGS_FILE_NAME;
	
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEE MMM d HH:mm:ss z yyyy");
	private static File newDirectoryFile = new File(DEFAULT_FILE_DIRECTORY);
	private static File storageFile = new File(DEFAULT_STORAGE_FILE_PATH);
	
	
	private String storedFilePath;
	private String folderPath;
	private UserTaskList userTaskList;
	private ArrayList<Task> taskList;
	private ArrayList<Task> doneTaskList;
	private ArrayList<Task> undoneTaskList;
	
	final GsonBuilder gsonBuilder = new GsonBuilder();
    final Gson gson;

	public Storage() throws IOException {
		//Initialization of gson class with Adapter
	    gsonBuilder.registerTypeAdapter(UserTaskList.class, new TaskAdapter());
	    gsonBuilder.setPrettyPrinting();
	    gson = gsonBuilder.create();	    
	    
		createNewDirectoryFolder();
		createNewStorageTxt();
		String storageJsonString = FileHandler.getStringFromFile(DEFAULT_STORAGE_FILE_PATH,
				StandardCharsets.UTF_8);
		
		userTaskList = deserializeJsonString(storageJsonString);
		
	}

	public UserTaskList getUserTaskList() {
		return userTaskList;
	}

	public void setUserTaskList(UserTaskList userTaskList) {
		this.userTaskList = userTaskList;
	}

	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(ArrayList<Task> taskList) {
		this.taskList = taskList;
	}

	public ArrayList<Task> getDoneTaskList() {
		return doneTaskList;
	}

	public void setDoneTaskList(ArrayList<Task> doneTaskList) {
		this.doneTaskList = doneTaskList;
	}

	public ArrayList<Task> getUndoneTaskList() {
		return undoneTaskList;
	}

	public void setUndoneTaskList(ArrayList<Task> undoneTaskList) {
		this.undoneTaskList = undoneTaskList;
	}

	private boolean createNewDirectoryFolder() {
		if (!newDirectoryFile.exists()) {
			newDirectoryFile.mkdir();
		}
		return true;
	}

	private boolean createNewStorageTxt() throws IOException {
		if (!storageFile.exists()) {
			storageFile.createNewFile();
		}
		
		return true;
	}
	public void store(UserTaskList lst) throws IOException{
		final String json = gson.toJson(lst);
	    FileHandler.writeStringToFile(storageFile, json);
	    
	}
	
	
	private UserTaskList deserializeJsonString(String jsonString){	
		
		if(jsonString.equals("")) return null;
	    final UserTaskList userTaskList = gson.fromJson(jsonString, UserTaskList.class);
		taskList = new ArrayList<Task>(Arrays.asList(userTaskList.getTaskArray()));
		return userTaskList;
		
	}

}
