package Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.Item;
import utils.ItemAdapter;
import utils.UserItemList;

public class TestExample {
	//Basic example to show my teammates how the gson library works. 
	public static void main(String[] args) {
	    final GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(UserItemList.class, new ItemAdapter());
	    gsonBuilder.setPrettyPrinting();

	    final Gson gson = gsonBuilder.create();
	    
	    UserItemList taskList = new UserItemList();
	    final Item task1 = new Item(1L,"EVENT","It is a sunny day", "High", "I want to swim","Done","red label",
	    		new Date(), new Date());
	    
	    final Item task2 = new Item(2L,"It is a rainny day", "Medium", "I want to study","Undone"
	    		,"blue label", new Date(), new Date());
	    taskList.setUserName("Wei Lip");	    
	    
	    ArrayList<Item> tArray = new ArrayList<>();
		tArray.add(task1);
		tArray.add(task2);
		taskList.setTaskArray(tArray);
	    final String json = gson.toJson(taskList);
	    System.out.println("Serialised");
	    System.out.println(json);
	    
	    final UserItemList userTaskList = gson.fromJson(json, UserItemList.class);
	    System.out.println("\nDeserialised");
	    userTaskList.getTaskArray().get(0).printInfo();
	    userTaskList.getTaskArray().get(1).printInfo();
	    
	   // Collections.sort(parsedBook.getTaskArrayList());
	}
}
