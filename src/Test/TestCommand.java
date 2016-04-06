package Test;

import java.io.InvalidClassException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import command.AddCommand;
import command.DelCommand;
import command.EditCommand;
import command.InvalidCommand;
import command.MultiDelCommand;
import command.MultiEditCommand;
import command.PathCommand;
import command.SearchCommand;
import command.UndoCommand;
import command.ViewCommand;
import main.POMPOM;
import parser.DateTimeParser;
import utils.Item;

import static org.junit.Assert.assertEquals;

/**
 * @@author wen hao
 *
 */
public class TestCommand {

	Date currentDate = new Date();
	DateTimeParser startParser = new DateTimeParser("start", "1 apr");
	Date startDate = startParser.getDate();
	
	
	DateTimeParser endParser = new DateTimeParser("end", "4 june");
	Date endDate = endParser.getDate();
	
	public static Calendar dateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	@Test
	public void testAdd() {
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

	}

	@Test
	public void testDeleteById() {
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
	public void testDeleteByTitle() {
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

		DelCommand delete = new DelCommand(addedTask.getTitle());

		// check if the delete command returns the right status message
		assertEquals("All tasks with title \"" + addedTask.getTitle() + "\" have been deleted", delete.execute());

		// check if the item was really deleted
		assertEquals(0, taskList.size());
	}

	@Test
	public void testMultiDelete() {
		POMPOM pompom = new POMPOM();
		AddCommand add_0 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "2d drawing", "low", "ongoing", "lab 1",
				currentDate, currentDate);
		AddCommand add_1 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "solar system", "medium", "ongoing", "lab 2",
				currentDate, currentDate);
		AddCommand add_2 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "3d drawing", "high", "ongoing", "lab 3",
				currentDate, currentDate);
		AddCommand add_3 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "high", "ongoing", "lab 4",
				currentDate, currentDate);
		AddCommand add_4 = new AddCommand(POMPOM.LABEL_TASK, "do cs2103t", "V0.2", "high", "ongoing", "deadline",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the add commands returns the right status message
		assertEquals("Task added", add_0.execute());
		assertEquals("Task added", add_1.execute());
		assertEquals("Task added", add_2.execute());
		assertEquals("Task added", add_3.execute());
		assertEquals("Task added", add_4.execute());

		Item addedTask_0 = taskList.get(0);
		Item addedTask_1 = taskList.get(1);
		Item addedTask_2 = taskList.get(2);
		Item addedTask_3 = taskList.get(3);
		Item addedTask_4 = taskList.get(4);

		DelCommand delete_0 = new DelCommand(addedTask_0.getId());
		DelCommand delete_1 = new DelCommand(addedTask_1.getId());
		DelCommand delete_2 = new DelCommand(addedTask_2.getId());
		DelCommand delete_3 = new DelCommand(addedTask_3.getId());
		DelCommand delete_4 = new DelCommand(addedTask_4.getId());
		ArrayList<DelCommand> deleteList = new ArrayList<DelCommand>();
		deleteList.add(delete_0);
		deleteList.add(delete_1);
		deleteList.add(delete_2);
		deleteList.add(delete_3);
		deleteList.add(delete_4);
		MultiDelCommand multiDelete = new MultiDelCommand(deleteList);

		// check if the delete command returns the right status message
		assertEquals("5 tasks has been deleted.", multiDelete.execute());

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
				currentDate, endDate);

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
	public void testEditStartDate() {
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

		EditCommand edit = new EditCommand(addedTask.getId(), "start date", startDate);

		// check if the edit command returns the right status message
		assertEquals(addedTask.getId() + ". was successfully edited", edit.execute());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date sdate = sdf.parse(startDate.toString());
			String date = sdate.toString();
			
			// check if the edit command did edit the actual item
			assertEquals("20160401", date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@Test
	public void testMultiEdit() {
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

		EditCommand edit_0 = new EditCommand(addedTask.getId(), "title", "do cs3241 lab 5");
		EditCommand edit_1 = new EditCommand(addedTask.getId(), "description", "build your town");
		EditCommand edit_2 = new EditCommand(addedTask.getId(), "priority", "high");

		ArrayList<EditCommand> editList = new ArrayList<EditCommand>();
		editList.add(edit_0);
		editList.add(edit_1);
		editList.add(edit_2);

		MultiEditCommand multiEdit = new MultiEditCommand(editList);

		// check if the edit command returns the right status message
		assertEquals("Multiple fields has been edited", multiEdit.execute());

		// check if the edit command did edit the actual item
		assertEquals("do cs3241 lab 5", addedTask.getTitle());
		assertEquals("build your town", addedTask.getDescription());
		assertEquals("high", addedTask.getPriority());

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
		assertEquals("Previous action was successfully undone", undo.execute());

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
		assertEquals("Previous action was successfully undone", undo.execute());

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
		assertEquals("Previous action was successfully undone", undo.execute());

		// check if the title changed back to previous title
		assertEquals("do cs3241", addedTask.getTitle());

	}

	@Test
	public void testSearch() {
		POMPOM pompom = new POMPOM();
		AddCommand command_0 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "2d drawing", "low", "ongoing", "lab 1",
				currentDate, currentDate);
		AddCommand command_1 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "solar system", "medium", "ongoing",
				"lab 2", currentDate, currentDate);
		AddCommand command_2 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "3d drawing", "high", "ongoing", "lab 3",
				currentDate, currentDate);
		AddCommand command_3 = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "high", "ongoing",
				"lab 4", currentDate, currentDate);
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

	@Test
	public void testView() {

		ViewCommand view = new ViewCommand("completedevent");
		assertEquals("CompletedEvent tab has been selected for viewing.", view.execute());
		assertEquals(POMPOM.LABEL_COMPLETED_EVENT, POMPOM.getCurrentTab());

		view = new ViewCommand("completedtask");
		assertEquals("CompletedTask tab has been selected for viewing.", view.execute());
		assertEquals(POMPOM.LABEL_COMPLETED_TASK, POMPOM.getCurrentTab());

		view = new ViewCommand("event");
		assertEquals("Event tab has been selected for viewing.", view.execute());
		assertEquals(POMPOM.LABEL_EVENT, POMPOM.getCurrentTab());

		view = new ViewCommand("task");
		assertEquals("Task tab has been selected for viewing.", view.execute());
		assertEquals(POMPOM.LABEL_TASK, POMPOM.getCurrentTab());

		view = new ViewCommand("search");
		assertEquals("Search tab has been selected for viewing.", view.execute());
		assertEquals(POMPOM.LABEL_SEARCH, POMPOM.getCurrentTab());

	}

	@Test
	public void testSetPath() {

		String currentPath = POMPOM.getStorage().getStorageFilePath();
		PathCommand path = new PathCommand(currentPath);
		assertEquals(String.format("Storage path set to: %s", currentPath), path.execute());

	}

	@Test
	public void testInvalid() {

		InvalidCommand invalid = new InvalidCommand("asd");
		assertEquals("asd is not a valid command", invalid.execute());

	}

}
