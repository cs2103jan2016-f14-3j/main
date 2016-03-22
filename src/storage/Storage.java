package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;

import javax.naming.InitialContext;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.javafx.util.Logging;

import utils.FileHandler;
import utils.Item;
import utils.ItemAdapter;
import utils.Settings;
import utils.SettingsAdapter;
import utils.UserTaskList;

/**
 * @author A0121628L
 *
 */
public class Storage {
	private final String DEFAULT_FILE_DIRECTORY = "PomPom Storage & Settings";
	private final String DEFAULT_FILE_NAME = "Storage.txt";
	private final String DEFAULT_STORAGE_FILE_PATH = DEFAULT_FILE_DIRECTORY
			+ "/" + DEFAULT_FILE_NAME;

	// storageFile is not final as user can reset storage file path
	private final File DEFAULT_DIRECTORY_FILE = new File(DEFAULT_FILE_DIRECTORY);
	private File storageFile = new File(DEFAULT_STORAGE_FILE_PATH);
	private String storageFilePath = DEFAULT_STORAGE_FILE_PATH;

	private final String SETTINGS_FILE_NAME = "settings.txt";
	private final String SETTINGS_FILE_PATH = DEFAULT_FILE_DIRECTORY + "/"
			+ SETTINGS_FILE_NAME;

	private final File settingsFile = new File(SETTINGS_FILE_PATH);
	private Settings settings;

	// The main data that is being extracted to this objects
	private UserTaskList userTaskList;
	private ArrayList<Item> taskList;
	private long idCounter;

	// Gson Library objects to read settings and storage file in json format
	final GsonBuilder GSON_ITEM_BUILDER = new GsonBuilder();
	final GsonBuilder GSON_SETTINGS_BUILDER = new GsonBuilder();
	private Gson gsonItem;
	private Gson gsonSettings;

	private static Logger logger = Logger.getLogger("Storage");

	public Storage() throws IOException {

	}

	public void init() throws IOException {
		initializeGsonObjects();
		initializeSettings();		
		initializeStorage();

		// Loading NLP library for parser for quick access
		PrettyTimeParser timeParser = new PrettyTimeParser();
		timeParser.parseSyntax("next year");
	}

	// Getters and Setters
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

	public long getIdCounter() {
		idCounter = idCounter + 1;
		return idCounter;
	}

	public void setIdCounter(long idCounter) {
		this.idCounter = idCounter;
	}

	public void setStorageFile(File storageFile) {
		this.storageFile = storageFile;
	}

	public void setStorageFilePath(String storageFilePath) {
		this.storageFilePath = storageFilePath;
	}

	// Initializing the GSON objects to read the storage text data file in JSON
	private void initializeGsonObjects() {
		GSON_ITEM_BUILDER.registerTypeAdapter(UserTaskList.class,
				new ItemAdapter());
		GSON_ITEM_BUILDER.setPrettyPrinting();
		gsonItem = GSON_ITEM_BUILDER.create();

		GSON_SETTINGS_BUILDER.registerTypeAdapter(Settings.class,
				new SettingsAdapter());
		GSON_SETTINGS_BUILDER.setPrettyPrinting();
		gsonSettings = GSON_ITEM_BUILDER.create();
	}
	
	private void initializeSettings() {
		//Check set directory folder exisit a not if not create folder
		checkDirectoryFolder();
		try {
			//Check Settings file exist a not if not create folder
			checkSettingsFile();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failure Settings Path: " + SETTINGS_FILE_PATH);
			e.printStackTrace();
		}
		String settingsString = null;
		try {
			settingsString = FileHandler.getStringFromFile(
					SETTINGS_FILE_PATH, StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failure Reading Settings ");
			e.printStackTrace();
		}
		//
		settings = deserializeSettingsString(settingsString);
	}

	private void initializeStorage() throws IOException {
		if (hasStoragePath()) {
			setStorageFile(new File(settings.getStoragePath()));
			setStorageFilePath(settings.getStoragePath());
		}
		checkStorageFile();
		String storageString = FileHandler.getStringFromFile(storageFilePath,
				StandardCharsets.UTF_8);
		userTaskList = deserializeStorageString(storageString);
		taskList = userTaskList.getTaskArray();
		idCounter = userTaskList.getIdCounter();
	}

	private boolean hasStoragePath() {
		return settings.getStoragePath() != null;
	}
	// If do not exist make directory
	private boolean checkDirectoryFolder() {
		if (!DEFAULT_DIRECTORY_FILE.exists()) {
			DEFAULT_DIRECTORY_FILE.mkdir();
			return false;
		}
		return true;
	}
	// If Storage file do not exist make file
	private boolean checkStorageFile() throws IOException {
		if (!storageFile.exists()) {
			storageFile.createNewFile();
			return false;
		}
		return true;
	}
	// If settings file do not exist make file
	private boolean checkSettingsFile() throws IOException {
		if (!settingsFile.exists()) {
			settingsFile.createNewFile();
		}
		return true;
	}
	private boolean checkEmptyString(String string){
		if (string.equals("")|| string.equals(null)){
			return true;
		}else{
			return false;
		}
		
		
	}
	
	private UserTaskList deserializeStorageString(String jsonString) {
		if (checkEmptyString(jsonString)) {
			UserTaskList utl = new UserTaskList("Not Set",
					new ArrayList<Item>());
			utl.setIdCounter(0);
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
			return newSettings;
		}

		return gsonSettings.fromJson(jsonString, Settings.class);

	}

	 //Obsolete method for now
	 public void store(UserTaskList lst) throws IOException {
	 final String json = gsonItem.toJson(lst);
	 FileHandler.writeStringToFile(storageFile, json);
	
	 }
	
	public void saveStorage() throws IOException {
		userTaskList.setTaskArray(taskList);
		userTaskList.setIdCounter(idCounter);
		final String json = gsonItem.toJson(userTaskList);
		FileHandler.writeStringToFile(storageFile, json);
	}


	public void saveSettings() throws IOException {
		settings.setStoragePath(storageFilePath);
		final String json = gsonSettings.toJson(settings);
		FileHandler.writeStringToFile(settingsFile, json);
	}

}
