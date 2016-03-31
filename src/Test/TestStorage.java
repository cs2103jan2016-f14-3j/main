package Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.InitialContext;
import javax.xml.stream.events.StartDocument;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import storage.Storage;
import utils.FileHandler;
import utils.Item;
import utils.ItemAdapter;
import utils.Settings;
import utils.SettingsAdapter;
import utils.UserItemList;
import static org.junit.Assert.*;

/**
 * @@author A0121628L
 *
 */
public class TestStorage {
	// Storage directory for testing
	private final String DEFAULT_FILE_DIRECTORY = "test";
	private final String DEFAULT_FILE_NAME = "Storage.txt";
	private final String DEFAULT_STORAGE_FILE_PATH_TESTING = DEFAULT_FILE_DIRECTORY
			+ "/" + DEFAULT_FILE_NAME;

	// Gson testing for reading and saving
	GsonBuilder gsonItemBuilder;
	Gson gsonItem;
	UserItemList taskListInput;

	Item item1;
	Item item2;
	Item item3 = new Item();
	Item item4 = new Item();
	Storage storageStub;
	String originalStorageFilePath;

	// Initialization of stub and item variables for adding later
	@Before
	public void initGson() {

		// Create a gson String builder to create a text . we register
		// UserItemList to write this
		// Object into a string. So a Gsonbuilder will create a instance of gson
		// to do this.
		gsonItemBuilder = new GsonBuilder();
		gsonItemBuilder.registerTypeAdapter(UserItemList.class,
				new ItemAdapter());
		gsonItemBuilder.setPrettyPrinting();
		gsonItem = gsonItemBuilder.create();
	}

	@Before
	public void initStorage() throws ParseException, IOException {
		// Change the storage file path for testing to another folder so that it
		// would
		// not affect the actual folder data
		storageStub = new Storage();
		storageStub.init();
		// Save the orignal file path and change back to it later
		originalStorageFilePath = storageStub.getStorageFilePath();
		// Create new storage for test
		File testFile = new File(DEFAULT_STORAGE_FILE_PATH_TESTING);
		testFile.delete();
		// Set to testing location and initialize storage
		storageStub.setStorageFilePath(DEFAULT_STORAGE_FILE_PATH_TESTING);
		storageStub.saveSettings();
		// Reinitialize storage with new storagefile
		storageStub.init();

		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
		sdf.parse("Wed, 4 Jul 2016 12:08");

		item1 = new Item(1L, "Event", "Swim", "High", "Nice day", "Done",
				"red label", sdf.parse("Wed, 04 Jan 2016 12:08"),
				sdf.parse("Wed, 30 Jan 2016 12:08"));

		item2 = new Item(2L, "Task", "Sleep", "Medium", "yawn", "Undone",
				"blue label", sdf.parse("Wed, 04 Jul 2016 12:08"),
				sdf.parse("Wed, 11 Jul 2016 12:08"));

		item2 = new Item(3L, "Task", "Fight", "Medium", "Muay thai", "Undone",
				"blue label", sdf.parse("Wed, 01 Jul 2016 12:08"),
				sdf.parse("Wed, 10 Jul 2016 12:08"));

		item2 = new Item(4L, "Task", "Mug", "Medium", "I want to study",
				"Undone", "blue label", new Date(), new Date());
	}

	/********************* Helper Methods * @throws IOException **************************************/
	public boolean isSameItemList(ArrayList<Item> listA, ArrayList<Item> listB) {
		int lengthA = listA.size();
		int lengthB = listB.size();
		boolean sameList = true;
		if (lengthA != lengthB) {
			return false;
		} else {
			// Test every aspect of the item value are equals to each other
			for (int i = 0; i < lengthA; i++) {
				// If the previous item was not the same return false.
				if (!sameList) {
					return false;
				}
				Item currentItemA = listA.get(0);
				Item currentItemB = listB.get(0);

				sameList = currentItemA.getId().equals(currentItemB.getId());
				sameList = currentItemA.getTitle().equals(
						currentItemB.getTitle());
				sameList = currentItemA.getDescription().equals(
						currentItemB.getDescription());
				sameList = currentItemA.getLabel().equals(
						currentItemB.getLabel());
				sameList = currentItemA.getPriority().equals(
						currentItemB.getPriority());
				sameList = currentItemA.getTitle().equals(
						currentItemB.getTitle());
				sameList = currentItemA.getTitle().equals(
						currentItemB.getTitle());
			}
			return sameList;
		}

	}

	/********************* UNIT TEST CASES * @throws IOException **************************************/
	// tests whether the file specified will be created when the constructor is
	// called.

	@Test
	public void testInitialization() throws IOException {
		Storage testStorage = new Storage();
		testStorage.init();
		assertNotNull(testStorage.getIdCounter());
		assertNotNull(testStorage.getStorageFilePath());
		assertNotNull(testStorage.getTaskList());
		assertNotNull(testStorage.getUserTaskList());
	}

	// Test whether getIdCounter increments itself a not
	@Test
	public void correctIdRead() throws IOException {
		assertEquals(storageStub.getUserTaskList().getIdCounter() + 1,
				storageStub.getIdCounter());
	}

	// The code to initialize the storageFilePath is done in the method
	// initstorage
	// now we will just check whether the path is set correctly a not
	@Test
	public void testWriteToSettings() throws SecurityException, IOException {
		assertEquals(storageStub.getStorageFilePath(),
				DEFAULT_STORAGE_FILE_PATH_TESTING);
	}

	// We add the items to storage and save it. And test whether we can get back
	// the same items
	// using our gson file reader.
	@Test
	public void testAddAndSave() throws IOException {
		storageStub.getTaskList().add(item1);
		storageStub.getTaskList().add(item2);
		storageStub.getTaskList().add(item3);
		storageStub.getTaskList().add(item4);
		storageStub.saveStorage();
		// Read Storage file at location storageFilePath(Tested above) and
		// check for equivalence by reading storage file with Gson
		String jsonUserItemList = FileHandler.getStringFromFile(
				DEFAULT_STORAGE_FILE_PATH_TESTING, StandardCharsets.UTF_8);
		UserItemList taskListOutput = gsonItem.fromJson(jsonUserItemList,
				UserItemList.class);
		assertTrue(isSameItemList(storageStub.getTaskList(),
				taskListOutput.getTaskArray()));
	}

	@Test
	public void testDeleteAndSave() throws IOException {
		storageStub.getTaskList().add(item1);
		storageStub.getTaskList().add(item2);
		storageStub.getTaskList().add(item3);
		storageStub.getTaskList().add(item4);
		storageStub.getTaskList().remove(2);
		storageStub.getTaskList().remove(1);
		storageStub.saveStorage();

		// Same code as above to check for equivalence
		String jsonUserItemList = FileHandler.getStringFromFile(
				DEFAULT_STORAGE_FILE_PATH_TESTING, StandardCharsets.UTF_8);
		UserItemList taskListOutput = gsonItem.fromJson(jsonUserItemList,
				UserItemList.class);
		assertTrue(isSameItemList(storageStub.getTaskList(),
				taskListOutput.getTaskArray()));
	}

	@Test
	public void testEditAndSave() throws IOException {
		storageStub.getTaskList().add(item1);
		storageStub.getTaskList().add(item2);
		storageStub.getTaskList().add(item3);
		storageStub.getTaskList().add(item4);
		storageStub.getTaskList().get(0).setTitle("Testing title");
		storageStub.getTaskList().get(0).setTitle("Testing title 2");
		storageStub.saveStorage();

		// Same code as above to check for equivalence
		String jsonUserItemList = FileHandler.getStringFromFile(
				DEFAULT_STORAGE_FILE_PATH_TESTING, StandardCharsets.UTF_8);
		UserItemList taskListOutput = gsonItem.fromJson(jsonUserItemList,
				UserItemList.class);
		assertTrue(isSameItemList(storageStub.getTaskList(),
				taskListOutput.getTaskArray()));
	}

	// Change back the settings of the original User or tester
	@After
	public void returnToOriginalSettings() throws IOException {
		storageStub.setStorageFilePath(originalStorageFilePath);
		storageStub.saveSettings();
	}
}
