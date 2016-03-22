package Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import command.AddCommand;
import command.DelCommand;
import command.EditCommand;
import command.SearchCommand;
import command.UndoCommand;
import main.POMPOM;
import utils.Item;

public class TestCommand {

	Date currentDate = new Date();

	@Test
	public void testAdd() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the “add” command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

	}

	@Test
	public void testDelete() {
		POMPOM pompom = new POMPOM();
		AddCommand add = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		add.execute();

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		DelCommand delete = new DelCommand(addedTask.getId());

		// check if the delete command returns the right status message
		assertEquals(addedTask.getId() + ". has been deleted from " + addedTask.getType(), delete.execute());

		// check if the item was really deleted
		assertEquals(0, taskList.size());
	}
	
	@Test
	public void testEditTitle() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		EditCommand edit = new EditCommand(addedTask.getId(), "title", "do cs2103t");

		// check if the edit command returns the right status message
		assertEquals(addedTask.getId() + ". was successfully edited", edit.execute());

		// check if the edit command did edit the actual item
		assertEquals("do cs2103t", addedTask.getTitle());

	}

	@Test
	public void testEditDescription() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		EditCommand edit = new EditCommand(addedTask.getId(), "description", "V0.2");

		// check if the edit command returns the right status message
		assertEquals(addedTask.getId() + ". was successfully edited", edit.execute());

		// check if the edit command did edit the actual item
		assertEquals("V0.2", addedTask.getDescription());

	}

	@Test
	public void testEditPriority() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		EditCommand edit = new EditCommand(addedTask.getId(), "priority", "high");

		// check if the edit command returns the right status message
		assertEquals(addedTask.getId() + ". was successfully edited", edit.execute());

		// check if the edit command did edit the actual item
		assertEquals("high", addedTask.getPriority());

	}

	@Test
	public void testEditStatus() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		EditCommand edit = new EditCommand(addedTask.getId(), "status", POMPOM.STATUS_COMPLETED);

		// check if the edit command returns the right status message
		assertEquals(addedTask.getId() + ". was successfully edited", edit.execute());

		// check if the edit command did edit the actual item
		assertEquals("completed", addedTask.getStatus());

	}

	@Test
	public void testEditLabel() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		EditCommand edit = new EditCommand(addedTask.getId(), "label", "deadline");

		// check if the edit command returns the right status message
		assertEquals(addedTask.getId() + ". was successfully edited", edit.execute());

		// check if the edit command did edit the actual item
		assertEquals("deadline", addedTask.getLabel());

	}

	@Test
	public void testUndoAdd() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		UndoCommand undo = new UndoCommand();

		// check if the undo command returns the right status message
		assertEquals("Previous action was successfully undid", undo.execute());

		// check if the taskList is empty because add was undid
		assertEquals(0, taskList.size());

	}

	@Test
	public void testUndoDelete() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		DelCommand delete = new DelCommand(addedTask.getId());

		// check if the delete command returns the right status message
		assertEquals(addedTask.getId() + ". has been deleted from " + addedTask.getType(), delete.execute());

		// check if the item was really deleted
		assertEquals(0, taskList.size());

		UndoCommand undo = new UndoCommand();

		// check if the undo command returns the right status message
		assertEquals("Previous action was successfully undid", undo.execute());

		// check if the taskList contain the recovered task
		Item recoveredTask = taskList.get(0);
		assertEquals("do cs3241", recoveredTask.getTitle());
		assertEquals("bezier curve", recoveredTask.getDescription());
		assertEquals("medium", recoveredTask.getPriority());
		assertEquals("ongoing", recoveredTask.getStatus());
		assertEquals("lab", recoveredTask.getLabel());

	}
	
	@Test
	public void testUndoEditTitle() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add command returns the right status message
		assertEquals("Task added", command.execute());

		// check if the taskList contain the added task
		Item addedTask = taskList.get(0);
		assertEquals("do cs3241", addedTask.getTitle());
		assertEquals("bezier curve", addedTask.getDescription());
		assertEquals("medium", addedTask.getPriority());
		assertEquals("ongoing", addedTask.getStatus());
		assertEquals("lab", addedTask.getLabel());

		EditCommand edit = new EditCommand(addedTask.getId(), "title", "do cs2103t");

		// check if the edit command returns the right status message
		assertEquals(addedTask.getId() + ". was successfully edited", edit.execute());

		// check if the edit command did edit the actual item
		assertEquals("do cs2103t", addedTask.getTitle());
		
		UndoCommand undo = new UndoCommand();

		// check if the undo command returns the right status message
		assertEquals("Previous action was successfully undid", undo.execute());

		// check if the title changed back to previous title
		assertEquals("do cs3241", addedTask.getTitle());

	}
	
	@Test
	public void testSearch() {
		POMPOM pompom = new POMPOM();
		AddCommand command_0 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "2d drawing", "low", "ongoing", "lab 1",
				currentDate, currentDate);
		AddCommand command_1 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "solar system", "medium", "ongoing", "lab 2",
				currentDate, currentDate);
		AddCommand command_2 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "3d drawing", "high", "ongoing", "lab 3",
				currentDate, currentDate);
		AddCommand command_3 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "high", "ongoing", "lab 4",
				currentDate, currentDate);
		AddCommand command_4 = new AddCommand(POMPOM.LABEL_TASK, "do cs2103t", "V0.2", "high", "ongoing", "deadline",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add commands returns the right status message
		assertEquals("Task added", command_0.execute());
		assertEquals("Task added", command_1.execute());
		assertEquals("Task added", command_2.execute());
		assertEquals("Task added", command_3.execute());
		assertEquals("Task added", command_4.execute());
		
		SearchCommand search = new SearchCommand("cs3241");
		
		// check if the search command returns the right status message
		assertEquals("Search resulted in 4 result(s).", search.execute());
		
		// check if the all search results contains the keyword 
		assertEquals(4, search.searchResults.size());
		for (int i = 0; i < 4; i++) {
			Item currentTask = search.searchResults.get(i);
			assertEquals(true, currentTask.getTitle().contains("cs3241"));
		}

	}

}
