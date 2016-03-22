package Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import storage.Storage;
import utils.FileHandler;
import utils.Item;
import utils.Settings;
import utils.SettingsAdapter;
import utils.UserItemList;

public class TestStorage {
	
	public static void main(String[] args) throws IOException {
		final GsonBuilder GSON_ITEM_BUILDER = new GsonBuilder();
		final GsonBuilder GSON_SETTINGS_BUILDER = new GsonBuilder();
		
		final String DEFAULT_FILE_DIRECTORY = "PomPom Storage & Settings";
		final String SETTINGS_FILE_NAME = "settings.txt";
		final String SETTINGS_FILE_PATH = DEFAULT_FILE_DIRECTORY
				+ "/" + SETTINGS_FILE_NAME;
		Gson gsonSettings;
		File settingsFile = new File(SETTINGS_FILE_PATH);
		
		Storage storageTest = new Storage();
		storageTest.init();
//		System.out.println(storageTest.getUserTaskList());
//		storageTest.getUserTaskList().printInfo();
		Item t1 = new Item(1L,"EVENT", "Title 1", "High", "Tryin to test","Done","red label", new Date(), new Date());
		Item t2 = new Item(2L,"EVENT", "Title 2111", "Medium", "Tryin to test 2","Done","red label",  new Date(), new Date());
		
		UserItemList userTaskList = new UserItemList();
		userTaskList.setUserName("Wei Lip");
		userTaskList.setIdCounter(3L);
		ArrayList<Item> tArray = new ArrayList<>();
		tArray.add(t1);
		tArray.add(t2);
		userTaskList.setTaskArray(tArray);
		storageTest.store(userTaskList);
		
//		Settings s = new Settings();
//		s.setStoragePath("C:\\Users\\Josh\\Desktop\\Nus sem 4\\CS2107\\Lecture notes\\Storage.txt");
		
//		GSON_SETTINGS_BUILDER.registerTypeAdapter(Settings.class,
//				new SettingsAdapter());
//		GSON_SETTINGS_BUILDER.setPrettyPrinting();
//		gsonSettings = GSON_ITEM_BUILDER.create();
//		
//		final String json = gsonSettings.toJson(s);
//		FileHandler.writeStringToFile(settingsFile, json);
//		
		
	}
}
