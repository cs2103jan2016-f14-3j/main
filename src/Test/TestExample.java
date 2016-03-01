package Test;

import java.util.Collections;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.Task;
import utils.TaskAdapter;
import utils.UserTaskList;

public class TestExample {
	public static void main(String[] args) {
	    final GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(UserTaskList.class, new TaskAdapter());
	    gsonBuilder.setPrettyPrinting();

	    final Gson gson = gsonBuilder.create();
	    
	    UserTaskList taskList = new UserTaskList();
	    final Task task1 = new Task("It is a sunny day", "High", "I want to swim",
	    		new Date(), new Date());
	    
	    final Task task2 = new Task("It is a rainny day", "Medium", "I want to study",
	    		new Date(), new Date());
	    taskList.setUserName("Wei Lip");
	    
	    taskList.setTaskArray(new Task[]{task1,task2});
	    

	    final String json = gson.toJson(taskList);
	    System.out.println("Serialised");
	    System.out.println(json);
	    
	    final UserTaskList parsedBook = gson.fromJson(json, UserTaskList.class);
	    System.out.println("\nDeserialised");
	    parsedBook.getTaskArray()[0].printInfo();
	    parsedBook.getTaskArray()[1].printInfo();
	    
	   // Collections.sort(parsedBook.getTaskArrayList());
	}
}
