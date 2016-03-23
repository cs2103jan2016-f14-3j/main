package Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import storage.Storage;
import utils.FileHandler;
import utils.Item;
import utils.Settings;
import utils.SettingsAdapter;
import utils.UserItemList;
import static org.junit.Assert.assertEquals;
public class TestStorage {
	// Initialized Gson items to test storage. 
//	final GsonBuilder GSON_ITEM_BUILDER = new GsonBuilder();
//	final GsonBuilder GSON_SETTINGS_BUILDER = new GsonBuilder();
//	
//	final String DEFAULT_FILE_DIRECTORY = "PomPom Storage & Settings";
//	final String SETTINGS_FILE_NAME = "settings.txt";
//	final String SETTINGS_FILE_PATH = DEFAULT_FILE_DIRECTORY
//			+ "/" + SETTINGS_FILE_NAME;
//	Gson gsonSettings;
//	File settingsFile = new File(SETTINGS_FILE_PATH);
//	Storage storageTest;
//	
//	Item t1 = new Item(1L,"EVENT", "Title 1", "High", "Tryin to test","Done","red label", new Date(), new Date());
//	Item t2 = new Item(2L,"EVENT", "Title 2111", "Medium", "Tryin to test 2","Done","red label",  new Date(), new Date());
//	
//
//	public void init() throws IOException{
//		storageTest = new Storage();
//		storageTest.init();
//	}
	// Test whether getIdCounter increments itself a not
	@Test
	public void correctIdRead() throws IOException{
		Storage testStorage = new Storage();
		testStorage.init();
		assertEquals(testStorage.getUserTaskList().getIdCounter() + 1, testStorage.getIdCounter());
	}
	// Check whether we can write the correct storage file path to storage a not
	@Test
	public void testWriteToProperties() throws SecurityException, IOException {
		Storage testStorage = new Storage();
		testStorage.init();
		final String TEST_FILE_PATH = "test/Testing";
		testStorage.setStorageFilePath(TEST_FILE_PATH);
		//testStorage.saveSettings();
		assertEquals(testStorage.getStorageFilePath(), TEST_FILE_PATH);
	}
	

	

}
