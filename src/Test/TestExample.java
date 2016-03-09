package Test;

import java.util.Collections;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.Item;
import utils.ItemAdapter;
import utils.UserTaskList;

public class TestExample {
	//Basic example to show my teammates how the gson library works. 
	public static void main(String[] args) {
	    final GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(UserTaskList.class, new ItemAdapter());
	    gsonBuilder.setPrettyPrinting();

	    final Gson gson = gsonBuilder.create();
	    
	    UserTaskList taskList = new UserTaskList();
	    final Item task1 = new Item(0,"It is a sunny day", "High", "I want to swim","Done","red label",
	    		new Date(), new Date());
	    
	    final Item task2 = new Item(5,"It is a rainny day", "Medium", "I want to study","Undone"
	    		,"blue label", new Date(), new Date());
	    taskList.setUserName("Wei Lip");	    
	    taskList.setTaskArray(new Item[]{task1,task2});
	    

	    final String json = gson.toJson(taskList);
	    System.out.println("Serialised");
	    System.out.println(json);
	    
	    final UserTaskList userTaskList = gson.fromJson(json, UserTaskList.class);
	    System.out.println("\nDeserialised");
	    userTaskList.getTaskArray()[0].printInfo();
	    userTaskList.getTaskArray()[1].printInfo();
	    
	   // Collections.sort(parsedBook.getTaskArrayList());
	}
}
