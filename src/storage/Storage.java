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
import java.util.logging.LoggingPermission;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.FileHandler;
import utils.Item;
import utils.ItemAdapter;
import utils.Settings;
import utils.SettingsAdapter;
import utils.UserTaskList;

public class Storage {

	private static final String DEFAULT_FILE_DIRECTORY = "PomPom Storage & Settings";
	private static final String DEFAULT_FILE_NAME = "Storage.txt";
	private static final String DEFAULT_STORAGE_FILE_PATH = DEFAULT_FILE_DIRECTORY
			+ "/" + DEFAULT_FILE_NAME;

	private static final String SETTINGS_FILE_NAME = "settings.txt";
	private static final String SETTINGS_FILE_PATH = DEFAULT_FILE_DIRECTORY
			+ "/" + SETTINGS_FILE_NAME;

	// private static final Logger log= Logger.getLogger(
	// Storage.class.getName() );
	// private static SimpleDateFormat dateFormat = new SimpleDateFormat(
	// "EEE MMM d HH:mm:ss z yyyy");
	private File newDirectoryFile = new File(DEFAULT_FILE_DIRECTORY);
	private File storageFile = new File(DEFAULT_STORAGE_FILE_PATH);
	private File settingsFile = new File(SETTINGS_FILE_PATH);


	private Settings settings;
	private UserTaskList userTaskList;
	private ArrayList<Item> taskList;
	private Long idCounter;

	// Gson Library objects to read settings and storage file in json format
	final GsonBuilder GSON_ITEM_BUILDER = new GsonBuilder();
	final GsonBuilder GSON_SETTINGS_BUILDER = new GsonBuilder();
	private Gson gsonItem;
	private Gson gsonSettings;

	public Storage() throws IOException {
		initializeGsonObjects();
		initializeSettings();
		initializeStorage();
	}

	public UserTaskList getUserTaskList() {
		return userTaskList;
	}

	public void setUserTaskList(UserTaskList userTaskList) {
		this.userTaskList = userTaskList;
	}

	public ArrayList<Item> getTaskList() {
		return taskList;
	}

	public void setTaskList(ArrayList<Item> taskList) {
		this.taskList = taskList;
	}
	public Long getIdCounter() {
		return idCounter;
	}

	public void setIdCounter(Long idCounter) {
		this.idCounter = idCounter;
	}
	
	private void initializeGsonObjects() {
		GSON_ITEM_BUILDER.registerTypeAdapter(UserTaskList.class,
				new ItemAdapter());
		GSON_ITEM_BUILDER.setPrettyPrinting();
		gsonItem = GSON_ITEM_BUILDER.create();

		GSON_SETTINGS_BUILDER.registerTypeAdapter(Settings.class,
				new SettingsAdapter());
		GSON_ITEM_BUILDER.setPrettyPrinting();
		gsonSettings = GSON_ITEM_BUILDER.create();

	}
	private void initializeSettings() throws IOException {
		createNewDirectoryFolder();
		createNewSettingsTxt();
		String settingsString = FileHandler.getStringFromFile(
				SETTINGS_FILE_PATH, StandardCharsets.UTF_8);
		settings = deserializeSettingsString(settingsString);
	}

	private void initializeStorage() throws IOException {
		if (settings.getStoragePath() != null) {
			storageFile = new File(settings.getStoragePath());		
		}
		createNewStorageTxt();	
		String storageString = FileHandler.getStringFromFile(
				DEFAULT_STORAGE_FILE_PATH, StandardCharsets.UTF_8);
		userTaskList = deserializeStorageString(storageString);
		taskList = userTaskList.getTaskArray();
		idCounter = userTaskList.getIdCounter();
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

	private boolean createNewSettingsTxt() throws IOException {
		if (!settingsFile.exists()) {
			settingsFile.createNewFile();
		}
		return true;
	}



	private UserTaskList deserializeStorageString(String jsonString) {
		if (jsonString.equals("")) {
			UserTaskList utl = new UserTaskList("Not Set", new ArrayList<Item>());
			taskList = new ArrayList<Item>();
			return utl;
		}
		UserTaskList userTaskList = gsonItem.fromJson(jsonString,
				UserTaskList.class);

		return userTaskList;


	}

	private Settings deserializeSettingsString(String jsonString) {
		if (jsonString.equals("")) {
			Settings newSettings = new Settings();
			idCounter = 1L;
			return newSettings;
		}		
		return gsonSettings.fromJson(jsonString, Settings.class);

	}
	public void store(UserTaskList lst) throws IOException {
		final String json = gsonItem.toJson(lst);
		FileHandler.writeStringToFile(storageFile, json);

	}


	public void save() throws IOException {		
		userTaskList.setTaskArray(taskList);
		userTaskList.setIdCounter(idCounter);
		final String json = gsonItem.toJson(userTaskList);
		FileHandler.writeStringToFile(storageFile, json);
	}
	
	public void addItemWithId(Item item){
		item.setId(idCounter);
		idCounter++;
		taskList.add(item);
	}





}
