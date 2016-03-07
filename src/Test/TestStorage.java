package Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import storage.Storage;
import utils.Task;
import utils.UserTaskList;

public class TestStorage {
	public static void main(String[] args) throws IOException {
		Storage storageTest = new Storage();
//		ArrayList<Task> test = storageTest.getTaskList();
//		test.get(1).printInfo();
//		test.get(0).printInfo();
		storageTest.getUserTaskList().printInfo();
		Task t1 = new Task("Title 1", "High", "Tryin to test","Done","red label", new Date(), new Date());
		Task t2 = new Task("Title 2111", "Medium", "Tryin to test 2","Done","red label",  new Date(), new Date());
		UserTaskList userTaskList = new UserTaskList();
		userTaskList.setUserName("Wei Lip");
		Task[] tArray = new Task[]{t1, t2};
		userTaskList.setTaskArray(tArray);
		storageTest.store(userTaskList);
	}
}
