package Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import storage.Storage;
import utils.Item;
import utils.UserTaskList;

public class TestStorage {
	public static void main(String[] args) throws IOException {
		Storage storageTest = new Storage();
		
//		storageTest.getUserTaskList().printInfo();
		Item t1 = new Item(0, "Title 1", "High", "Tryin to test","Done","red label", new Date(), new Date());
		Item t2 = new Item(0, "Title 2111", "Medium", "Tryin to test 2","Done","red label",  new Date(), new Date());
		
		UserTaskList userTaskList = new UserTaskList();
		userTaskList.setUserName("Wei Lip");
		Item[] tArray = new Item[]{t1, t2};
		userTaskList.setTaskArray(tArray);
		storageTest.store(userTaskList);
	}
}
