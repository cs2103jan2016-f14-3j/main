package Test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.Item;
import utils.ItemAdapter;
import utils.UserItemList;
import static org.junit.Assert.assertEquals;
public class GsonTest {
	// Basic example to show my teammates how the gson library works.
	GsonBuilder gsonBuilder;
	Gson gson;
	UserItemList taskList;
	Item task1;
	Item task2;
	long initialCounter;
	public void initObjects() {
		// Create a gson String builder to create a text file. we register
		// UserItemList to write this
		// Object into a string. So a Gsonbuilder will create a instance of gson
		// to do this.
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UserItemList.class, new ItemAdapter());
		gsonBuilder.setPrettyPrinting();
		gson = gsonBuilder.create();
		//Add userItemList data
	    taskList = new UserItemList();
	    task1 = new Item(1L,"Event","It is a sunny day", "High", "I want to swim","Done","red label",
	    		new Date(), new Date());
	    
	    task2 = new Item(2L,"Task","It is a rainny day", "Medium", "I want to study","Undone"
	    		,"blue label",  new Date(), new Date());
	    initialCounter = 0L;
	    taskList.setUserName("Wei Lip");
	    taskList.setIdCounter(initialCounter);	    
	    ArrayList<Item> tArray = new ArrayList<>();
		tArray.add(task1);
		tArray.add(task2);		
		taskList.setTaskArray(tArray);
	}
	//Write into a json string with the gson library and test when we read the same string
	//with the Gson library do we get the same object a not. 
	@Test
	public void testGson(){
		initObjects();
	    final String json = gson.toJson(taskList);
	    System.out.println("Serialised");
	    System.out.println(json);
	    // get Object from above json string and test whether it writes to the object with same values
	    //or not
	    final UserItemList uItemListResult = gson.fromJson(json, UserItemList.class);
	    System.out.println("\nDeserialised");
	    assertEquals(uItemListResult.getUserName(), taskList.getUserName());
	    assertEquals(uItemListResult.getIdCounter(), taskList.getIdCounter());
//	    assertEquals(uItemListResult.getTaskArray(), taskList.getTaskArray());
	    //Test whether the objects are the same a not
	    //ITEM 1
	    assertEquals(uItemListResult.getTaskArray().get(0).getDescription(), 
	    		taskList.getTaskArray().get(0).getDescription());
//	    assertEquals(uItemListResult.getTaskArray().get(0).getEndDate().compareTo(
//	    		taskList.getTaskArray().get(0).getEndDate()), 0);
	    assertEquals(uItemListResult.getTaskArray().get(0).getId(), 
	    		taskList.getTaskArray().get(0).getId());
	    assertEquals(uItemListResult.getTaskArray().get(0).getLabel(), 
	    		taskList.getTaskArray().get(0).getLabel());
	    assertEquals(uItemListResult.getTaskArray().get(0).getPriority(), 
	    		taskList.getTaskArray().get(0).getPriority());
//	    assertEquals(uItemListResult.getTaskArray().get(0).getStartDate(), 
//	    		taskList.getTaskArray().get(0).getStartDate());
	    assertEquals(uItemListResult.getTaskArray().get(0).getStatus(), 
	    		taskList.getTaskArray().get(0).getStatus());
	    assertEquals(uItemListResult.getTaskArray().get(0).getTitle(), 
	    		taskList.getTaskArray().get(0).getTitle());
	    assertEquals(uItemListResult.getTaskArray().get(0).getType(), 
	    		taskList.getTaskArray().get(0).getType());
	    //ITEM 2
	    assertEquals(uItemListResult.getTaskArray().get(1).getDescription(), 
	    		taskList.getTaskArray().get(1).getDescription());
//	    assertEquals(uItemListResult.getTaskArray().get(1).getEndDate(), 
//	    		taskList.getTaskArray().get(1).getEndDate());
	    assertEquals(uItemListResult.getTaskArray().get(1).getId(), 
	    		taskList.getTaskArray().get(1).getId());
	    assertEquals(uItemListResult.getTaskArray().get(1).getLabel(), 
	    		taskList.getTaskArray().get(1).getLabel());
	    assertEquals(uItemListResult.getTaskArray().get(1).getPriority(), 
	    		taskList.getTaskArray().get(1).getPriority());
//	    assertEquals(uItemListResult.getTaskArray().get(1).getStartDate(), 
//	    		taskList.getTaskArray().get(1).getStartDate());
	    assertEquals(uItemListResult.getTaskArray().get(1).getStatus(), 
	    		taskList.getTaskArray().get(1).getStatus());
	    assertEquals(uItemListResult.getTaskArray().get(1).getTitle(), 
	    		taskList.getTaskArray().get(1).getTitle());
	    assertEquals(uItemListResult.getTaskArray().get(1).getType(), 
	    		taskList.getTaskArray().get(1).getType()); 
	    
	    

	}


}
