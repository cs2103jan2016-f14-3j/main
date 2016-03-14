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
		Item t1 = new Item(1L, "Title 1", "High", "Tryin to test","Done","red label", new Date(), new Date());
		Item t2 = new Item(2L, "Title 2111", "Medium", "Tryin to test 2","Done","red label",  new Date(), new Date());
		
		UserTaskList userTaskList = new UserTaskList();
		userTaskList.setUserName("Wei Lip");
		userTaskList.setIdCounter(3L);
		ArrayList<Item> tArray = new ArrayList<>();
		tArray.add(t1);
		tArray.add(t2);
		userTaskList.setTaskArray(tArray);
		storageTest.store(userTaskList);
	}
}
