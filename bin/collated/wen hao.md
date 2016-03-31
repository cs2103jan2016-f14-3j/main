# wen hao
###### command\AddCommand.java
``` java
 *
 */
public class AddCommand extends Command {
	
	private static final String MESSAGE_TASK_ADDED = "%s added";	
	private Item task;
	private boolean isUndo;
	
	public AddCommand(String type, String title, String description, String priority, 
			String status, String label, Date startDate, Date endDate) {

		task = new Item();
		isUndo = false;
		assertNotNull(title);
		
		Long id = POMPOM.getStorage().getIdCounter();
		task.setId(id);
		task.setType(type);
		task.setTitle(title);
		task.setDescription(description);
		task.setPriority(priority);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		
		logger.log(Level.INFO, "AddCommand initialized");
	}
	
	public AddCommand(Long id, String type, String title, String description, String priority, 
			String status, String label, Date startDate, Date endDate) {

		task = new Item();
		isUndo = true;
		assertNotNull(title);
		
		task.setId(id);
		task.setType(type);
		task.setTitle(title);
		task.setDescription(description);
		task.setPriority(priority);
		task.setStatus(status);
		task.setLabel(label);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		
		logger.log(Level.INFO, "Counter action AddCommand initialized");
	}
	
	private void storeTask() {
		POMPOM.getStorage().getTaskList().add(task);
	}
	
	private Command createCounterAction() {
		DelCommand counterAction = new DelCommand(task.getId(), true);
		return counterAction;
	}
	
	private void updateUndoStack() {
		Command counterAction = createCounterAction();
		POMPOM.getUndoStack().push(counterAction);
	}
		
	public String execute() {
		
		if (!isUndo) updateUndoStack();
		storeTask();
		
		if (task.getType().equals(POMPOM.LABEL_EVENT)) {
			returnMsg = String.format(MESSAGE_TASK_ADDED, POMPOM.LABEL_EVENT);
			POMPOM.setCurrentTab(POMPOM.LABEL_EVENT);
		} else {
			returnMsg = String.format(MESSAGE_TASK_ADDED, POMPOM.LABEL_TASK);
			POMPOM.setCurrentTab(POMPOM.LABEL_TASK);
		}
		
		return returnMsg;
	}
	
}
```
###### command\AddRecurringCommand.java
``` java
 *
 */
public class AddRecurringCommand extends Command {

	private static final String MESSAGE_RECURRING = "A recurring task has been added";
	
	ArrayList<AddCommand> addList;
	
	public AddRecurringCommand(ArrayList<AddCommand> addList) {
		this.addList = addList;
	}
	
	public String execute() {
		for (int i = 0; i < addList.size(); i++) {
			addList.get(i).execute();
		}
		
		returnMsg = MESSAGE_RECURRING;
		return returnMsg;
	}
	
}
```
###### command\CleanUpCommand.java
``` java
 *
 */
public class CleanUpCommand extends Command {

	private static final String MESSAGE_CLEAN_UP = "The task list has been cleaned up; "
			+ "tasks with deleted status are removed and task ID are updated.";
	ArrayList<Item> taskList;
	
	public CleanUpCommand() {
		this.taskList = getTaskList();
	}
	

	public String execute() {
		
		int removeCount = 0;
		for (int i = 0; i < taskList.size(); i++) {
			Item currentTask = getTask(i);
			if (currentTask.getStatus().equals("deleted")) {
				taskList.remove(i);
				removeCount++;
			} else {
//				currentTask.setId(i+1-removeCount);
			}
		}
		
		returnMsg = MESSAGE_CLEAN_UP;
		return returnMsg;
		
	}
	
}
```
###### command\Command.java
``` java
 *
 */
public abstract class Command {

	protected static final String STATUS_PENDING = "pending";
	protected static final String STATUS_ONGOING = "ongoing";
	protected static final String STATUS_COMPLETED = "completed";
	protected static final String STATUS_OVERDUE = "overdue";
	protected String returnMsg = "";
	
	public static Logger logger = Logger.getLogger("Command");
	
	public Command() {
		
	}
	
	protected Item getTask(long taskId) {
		ArrayList<Item> taskList = getTaskList(); 
		for (int i  = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getId() == taskId) {
				return taskList.get(i);
			}
		}
		return null;
	}
	
	protected ArrayList<Item> getTaskList() {
		return POMPOM.getStorage().getTaskList();		
	}
	
	protected boolean checkExists(long taskId) {
		boolean exists;
		try {
			Item toDelete = getTask(taskId);
			exists = true;
		} catch (IndexOutOfBoundsException e) {
			exists =  false;
		}
		return exists;
	}
	
	public abstract String execute();
	
}
```
###### command\DelCommand.java
``` java
 *
 */
public class DelCommand extends Command {

	private static final String MESSAGE_TASK_DELETED = "%1s has been deleted from %2s";
	private static final String MESSAGE_TASK_ERROR = "Unable to delete task %s";
	private long taskId;
	private boolean isUndo;
	private boolean canDelete;
	private Item toDelete;

	public DelCommand(long taskId) {
		this.taskId = taskId;
		isUndo = false;

		logger.log(Level.INFO, "DelCommand initialized");
	}

	public DelCommand(long taskId, boolean isUndo) {
		this.taskId = taskId;
		this.isUndo = isUndo;

		logger.log(Level.INFO, "Counter action DelCommand initialized");
	}

	private void removeTask() {
		ArrayList<Item> taskList = getTaskList();
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getId() == taskId) {
				taskList.remove(i);
			}
		}
		POMPOM.getStorage().setTaskList(taskList);
	}

	private Command createCounterAction() {
		toDelete = getTask(taskId);
		AddCommand counterAction = new AddCommand(toDelete.getId(), toDelete.getType(), toDelete.getTitle(),
				toDelete.getDescription(), toDelete.getPriority(), toDelete.getStatus(), toDelete.getLabel(),
				toDelete.getStartDate(), toDelete.getEndDate());
		return counterAction;
	}

	private void updateUndoStack() {
		Command counterAction = createCounterAction();
		POMPOM.getUndoStack().push(counterAction);
	}

	public String execute() {
		canDelete = checkExists(taskId);
		if (canDelete) {

			if (!isUndo) {
				updateUndoStack();
				returnMsg = String.format(MESSAGE_TASK_DELETED, (taskId + "."), toDelete.getType());

				if (toDelete.getType().equals(POMPOM.LABEL_EVENT.toLowerCase())) {
					POMPOM.setCurrentTab(POMPOM.LABEL_EVENT);
				} else {
					POMPOM.setCurrentTab(POMPOM.LABEL_TASK);
				}
			}

			removeTask();

		} else {
			returnMsg = String.format(MESSAGE_TASK_ERROR, taskId);
		}
		return returnMsg;
	}

}
```
###### command\EditCommand.java
``` java
 *
 */
public class EditCommand extends Command {
	
	private static final String MESSAGE_TASK_EDITED = "%s. was successfully edited";	
	private static final String MESSAGE_TASK_ERROR = "Unable to edit task %s";
	
	private static final String FIELD_TYPE = "type";
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_PRIORITY = "priority";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_LABEL = "label";
	private static final String FIELD_START_DATE = "start date";
	private static final String FIELD_END_DATE = "end date";
	
	private long taskId;
	private String field;
	private String newData;
	private Date newDate;
	private Item task;
	private boolean canEdit;
	private boolean isUndo;
	
	public EditCommand(long taskId, String field, String newData) {
		this.taskId = taskId;
		this.task = getTask(taskId);
		this.field = field;
		this.newData = newData;
		isUndo = false;
		
		//logger.log(Level.INFO, "EditCommand initialized");
	}
	
	public EditCommand(long taskId, String field, Date newDate) {
		this.taskId = taskId;
		this.task = getTask(taskId);
		this.field = field;
		this.newDate = newDate;
		isUndo = false;
		
		//logger.log(Level.INFO, "EditCommand initialized");
	}
	
	public EditCommand(long taskId, String field, String newData, boolean isUndo) {
		this.taskId = taskId;
		this.task = getTask(taskId);
		this.field = field;
		this.newData = newData;
		this.isUndo = isUndo;
		
		//logger.log(Level.INFO, "Counter action EditCommand initialized");
	}
	
	public EditCommand(long taskId, String field, Date newDate, boolean isUndo) {
		this.taskId = taskId;
		this.task = getTask(taskId);
		this.field = field;
		this.newDate = newDate;
		this.isUndo = isUndo;
		
	//	logger.log(Level.INFO, "Counter action EditCommand initialized");
	}
	
	private void updateChanges() {		
		
		switch (field.toLowerCase()) {
		case FIELD_TITLE:
			task.setTitle(newData);			
			break;
		case FIELD_TYPE:
			task.setType(newData);
			break;
		case FIELD_DESCRIPTION:
			task.setDescription(newData);
			break;
		case FIELD_PRIORITY:
			task.setPriority(newData);
			break;
		case FIELD_STATUS:
			task.setStatus(newData);
			break;
		case FIELD_LABEL:
			task.setLabel(newData);
			break;
		case FIELD_START_DATE:
			task.setStartDate(newDate);
			break;
		case FIELD_END_DATE:
			task.setEndDate(newDate);
			break;
		}
		
	}
	
	private Command createCounterAction() {
		EditCommand counterAction;
		
		switch (field.toLowerCase()) {
		case FIELD_TITLE:
			counterAction = new EditCommand(taskId, field, task.getTitle(), true);			
			break;
		case FIELD_DESCRIPTION:
			counterAction = new EditCommand(taskId, field, task.getDescription(), true);
			break;
		case FIELD_PRIORITY:
			counterAction = new EditCommand(taskId, field, task.getPriority(), true);
			break;
		case FIELD_STATUS:
			counterAction = new EditCommand(taskId, field, task.getStatus(), true);
			break;
		case FIELD_LABEL:
			counterAction = new EditCommand(taskId, field, task.getLabel(), true);
			break;
		case FIELD_START_DATE:
			counterAction = new EditCommand(taskId, field, task.getStartDate(), true);
			break;
		case FIELD_END_DATE:
			counterAction = new EditCommand(taskId, field, task.getEndDate(), true);
			break;
		default:
			counterAction = null;
			break;
		}
		
		return counterAction;
		
	}
	
	private void updateUndoStack() {
		Command counterAction = createCounterAction();
		POMPOM.getUndoStack().push(counterAction);
	}
	
	
	public String execute() {
		canEdit = checkExists(taskId);
		
		if (canEdit) {
			if (!isUndo) updateUndoStack();
			updateChanges();
			returnMsg = String.format(MESSAGE_TASK_EDITED, taskId);
			ArrayList<Item> taskList = getTaskList();
			POMPOM.getStorage().setTaskList(taskList);
		} else {
			returnMsg = MESSAGE_TASK_ERROR;
		}
		return returnMsg;
	}
	
}
```
###### command\ExitCommand.java
``` java
 *
 */
public class ExitCommand {
	
	public ExitCommand() {
		try {
			POMPOM.getStorage().saveStorage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
```
###### command\InvalidCommand.java
``` java
 *
 */
public class InvalidCommand extends Command{

	private String error;
	
	public InvalidCommand(String error) {
		this.error = error;
	}
	
	public String execute() {
		String returnMsg = error;
		return returnMsg;
	}

	
	
}
```
###### command\MultiDelCommand.java
``` java
 *
 */
import java.util.ArrayList;

public class MultiDelCommand extends Command {

	private static final String MESSAGE_MULTIDEL = "%s tasks has been deleted.";
	
	ArrayList<DelCommand> deleteList;
	
	public MultiDelCommand(ArrayList<DelCommand> deleteList) {
		this.deleteList = deleteList;
	}
	
	public String execute() {
		
		for (int i = 0; i < deleteList.size(); i++) {
			deleteList.get(i).execute();
		}
		
		returnMsg = String.format(MESSAGE_MULTIDEL, deleteList.size());
		return returnMsg;
	}
	
}
```
###### command\MultiEditCommand.java
``` java
 *
 */
import java.util.ArrayList;

public class MultiEditCommand extends Command {

	private static final String MESSAGE_MULTIEDIT = "Multiple fields of task %s has been edited";
	
	ArrayList<EditCommand> editList;
	
	public MultiEditCommand(ArrayList<EditCommand> editList) {
		this.editList = editList;
	}
	
	public String execute() {
		for (int i = 0; i < editList.size(); i++) {
			editList.get(i).execute();
		}
		
		returnMsg = String.format(MESSAGE_MULTIEDIT, editList.size());
		return returnMsg;
	}
	
}
```
###### command\PathCommand.java
``` java
 *
 */
import java.io.IOException;

import main.POMPOM;

public class PathCommand extends Command {
	
	private static final String MESSAGE_SET_PATH = "Storage path set to: %s";
	
	private String storageFilePath;
	
	public PathCommand(String storageFilePath) {
		this.storageFilePath = storageFilePath;
	}
	
	@Override
	public String execute() {
		
		POMPOM.getStorage().setStorageFilePath(storageFilePath);
		try {
			POMPOM.getStorage().saveSettings();
			POMPOM.getStorage().init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		returnMsg = String.format(MESSAGE_SET_PATH, storageFilePath);
		return returnMsg;
	}
	
}
```
###### command\RestoreCommand.java
``` java
 *
 */
import java.util.Date;

import utils.Item;

public class RestoreCommand extends Command {
	
	private static final String MESSAGE_TASK_RESTORED = "%s has been restored";	
	private static final String MESSAGE_TASK_ERROR = "Unable to restore task %s";
	private int taskId;
	private String taskTitle;
	private boolean canDelete;
	
	public RestoreCommand(int taskId) {
		this.taskId = taskId-1;
	}
	
	private void removeTask() {
		Item task = getTask(taskId);
		Date currentDate = new Date();
		if (task.getEndDate().compareTo(currentDate) < 0) {
			task.setStatus(STATUS_PENDING);
		} else {
			task.setStatus(STATUS_OVERDUE);
		}
	}
	
	public String execute() {
		canDelete = checkExists(taskId);
		if (canDelete) {
			removeTask();
			returnMsg = String.format(MESSAGE_TASK_RESTORED, (taskId+". "+taskTitle));
		} else {
			returnMsg = String.format(MESSAGE_TASK_ERROR, taskId);
		}
		return returnMsg;
	}
	
}
```
###### command\SearchCommand.java
``` java
 *
 */
import java.util.ArrayList;

import main.POMPOM;
import utils.Item;

public class SearchCommand extends Command {
	
	private static final String MESSAGE_SEARCH = "Search resulted in %s result(s).";
	
	public ArrayList<Item> searchResults;
	private String keyword;
	
	public SearchCommand(String keyword) {
		this.searchResults = new ArrayList<Item>();
		this.keyword = keyword;
	}
	
	private ArrayList<Item> search() {
		
		ArrayList<Item> taskList = getTaskList();
		for (int i = 0; i < taskList.size(); i++) {
			Item currentTask = taskList.get(i);
			if (currentTask.getTitle().contains(keyword)) {
				searchResults.add(currentTask);
			}
		}
		
		return searchResults;
		
	}
	
	public String execute() {
		POMPOM.setSearchList(search());
		POMPOM.setCurrentTab(POMPOM.LABEL_SEARCH);
		returnMsg = String.format(MESSAGE_SEARCH, searchResults.size());
		return returnMsg;
	}

}
```
###### command\UndoCommand.java
``` java
 *
 */
import main.POMPOM;

public class UndoCommand extends Command{
	
	private static final String MESSAGE_UNDO = "Previous action was successfully undid";
	
	public String execute() {
		Command undo = (Command) POMPOM.getUndoStack().pop();
		undo.execute();
		returnMsg = MESSAGE_UNDO;
		return returnMsg;
	}

}
```
###### main\POMPOM.java
``` java
 *
 */
public class POMPOM {

	public static final String STATUS_PENDING = "pending";
	public static final String STATUS_ONGOING = "ongoing";
	public static final String STATUS_COMPLETED = "completed";
	public static final String STATUS_OVERDUE = "overdue";
	public static final String STATUS_FLOATING = "floating";

	public static final String LABEL_TASK = "Task";
	public static final String LABEL_COMPLETED_TASK = "CompletedTask";
	public static final String LABEL_EVENT = "Event";
	public static final String LABEL_COMPLETED_EVENT = "CompletedEvent";
	public static final String LABEL_SEARCH = "Search";

	private static Storage storage;
	private static Stack<Command> undoStack;
	public static PrettyTimeParser timeParser;
	public static ArrayList<Item> searchList;
	public static String currentTab;

	public POMPOM() {
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		try {
			storage = new Storage();
			storage.init();
			undoStack = new Stack<Command>();
			refreshStatus();
			timeParser = new PrettyTimeParser();
			timeParser.parseSyntax("next year");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void refreshStatus() {
		ArrayList<Item> taskList = storage.getTaskList();
		Date currentDate = new Date();

		for (int i = 0; i < taskList.size(); i++) {
			Item currentTask = taskList.get(i);
			Date taskStartDate = currentTask.getStartDate();
			Date taskEndDate = currentTask.getEndDate();

			if (taskStartDate == null && taskEndDate == null) {
				currentTask.setStatus(STATUS_FLOATING);

			} else if (taskStartDate == null) {

				if (currentDate.before(taskEndDate)) {
					currentTask.setStatus(STATUS_ONGOING);
				} else if (!currentTask.getStatus().equals(STATUS_COMPLETED)) {
					currentTask.setStatus(STATUS_OVERDUE);
				}

			} else if (taskEndDate == null) {

				if (taskStartDate.after(currentDate)) {
					currentTask.setStatus(STATUS_PENDING);
				} else if (taskStartDate.before(currentDate)
						&& isNotCompleted(currentTask)) {
					currentTask.setStatus(STATUS_ONGOING);
				}

			} else if (currentDate.before(taskStartDate)) {
				currentTask.setStatus(STATUS_PENDING);

			} else if (currentDate.compareTo(taskStartDate) >= 0
					&& currentDate.before(taskEndDate)
					&& isNotCompleted(currentTask)) {
				currentTask.setStatus(STATUS_ONGOING);

			} else if (currentDate.after(taskEndDate)
					&& isNotCompleted(currentTask)) {
				currentTask.setStatus(STATUS_OVERDUE);

			}
		}
	}

	public String execute(String input) {
		Parser parser = new Parser();
		Command command = parser.executeCommand(input);
		System.out.println(command);
		String returnMsg = command.execute();
		refreshStatus();
		return returnMsg;
	}

	public static String executeCommand(Command executable) {
		String returnMsg = executable.execute();
		refreshStatus();
		return returnMsg;
	}

	private static boolean isNotCompleted(Item item) {

		return !item.getStatus().equals(STATUS_COMPLETED);

	}

	public static PrettyTimeParser getTimeParser() {
		return timeParser;
	}

	public static void setTimeParser(PrettyTimeParser timeParser) {
		POMPOM.timeParser = timeParser;
	}

	public static Storage getStorage() {
		return storage;
	}

	public static Stack getUndoStack() {
		return undoStack;
	}

	public static void saveSettings(String storageFilePath) throws IOException {
		storage.setStorageFilePath(storageFilePath);
		storage.saveSettings();
	}

	public static String getCurrentTab() {
		return POMPOM.currentTab;
	}

	public static void setCurrentTab(String setTab) {
		POMPOM.currentTab = setTab;
	}

	public static ArrayList<Item> getSearchList() {
		return searchList;
	}

	public static void setSearchList(ArrayList<Item> searchList) {
		POMPOM.searchList = searchList;
	}

}
```
###### Test\TestCommand.java
``` java
 *
 */
public class TestCommand {

	Date currentDate = new Date();

	@Test
	public void testAdd() {
		POMPOM pompom = new POMPOM();
		AddCommand command = new AddCommand(POMPOM.LABEL_TASK, "do cs3241", "bezier curve", "medium", "ongoing", "lab",
				currentDate, currentDate);

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		// check if the â€œaddâ€� command returns the right status message
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
```
###### Test\TestPOMPOM.java
``` java
 *
 */
public class TestPOMPOM {

	@Test
	public void testStatusPending() {
		POMPOM pompom = new POMPOM();

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		pompom.execute("add do cs3241 f:tomorrow e:next friday");
		Item firstTask = taskList.get(0);

		pompom.execute("add do cs3241 f:next monday");
		Item secondTask = taskList.get(1);

		pompom.refreshStatus();
		/**
		 * check if the statuses are pending as current date is before start
		 * date
		 */
		assertEquals(POMPOM.STATUS_PENDING, firstTask.getStatus());
		assertEquals(POMPOM.STATUS_PENDING, secondTask.getStatus());

	}

	@Test
	public void testStatusOngoing() {
		POMPOM pompom = new POMPOM();

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		pompom.execute("add do cs3241 f:now e:next friday");
		Item firstTask = taskList.get(0);

		pompom.execute("add do cs3241 f:now");
		Item secondTask = taskList.get(1);

		pompom.execute("add do cs3241 next tuesday");
		Item thirdTask = taskList.get(2);

		pompom.refreshStatus();
		/**
		 * check if the statuses are ongoing as current date is within the start
		 * and end date specified
		 */
		assertEquals(POMPOM.STATUS_ONGOING, firstTask.getStatus());
		assertEquals(POMPOM.STATUS_ONGOING, secondTask.getStatus());
		assertEquals(POMPOM.STATUS_ONGOING, thirdTask.getStatus());

	}

	@Test
	public void testStatusOverdue() {
		POMPOM pompom = new POMPOM();

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		pompom.execute("add do cs3241 f:yesterday e:yesterday");
		Item firstTask = taskList.get(0);

		pompom.execute("add do cs3241 last monday");
		Item secondTask = taskList.get(1);

		pompom.refreshStatus();
		/**
		 * check if the statuses are overdue as current date after end date
		 */
		assertEquals(POMPOM.STATUS_OVERDUE, firstTask.getStatus());
		assertEquals(POMPOM.STATUS_OVERDUE, secondTask.getStatus());

	}

	@Test
	public void testStatusFloating() {
		POMPOM pompom = new POMPOM();

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		pompom.execute("add do cs3241");
		Item firstTask = taskList.get(0);

		pompom.execute("add do cs2103t");
		Item secondTask = taskList.get(1);

		pompom.refreshStatus();
		/**
		 * check if the statuses are floating as start and end date are not
		 * specified
		 */
		assertEquals(POMPOM.STATUS_FLOATING, firstTask.getStatus());
		assertEquals(POMPOM.STATUS_FLOATING, secondTask.getStatus());

	}

	@Test
	public void testStatusCompleted() {
		POMPOM pompom = new POMPOM();

		ArrayList<Item> taskList = POMPOM.getStorage().getTaskList();
		taskList.clear();

		pompom.execute("add do cs3241 f:now e:next friday");
		Item firstTask = taskList.get(0);

		pompom.execute("add do cs3241 f:now");
		Item secondTask = taskList.get(1);

		pompom.execute("add do cs3241 next tuesday");
		Item thirdTask = taskList.get(2);

		ArrayList<EditCommand> editList = new ArrayList<EditCommand>();
		for (int i = 0; i < 3; i++) {
			Item currentTask = taskList.get(i);
			EditCommand command = new EditCommand(currentTask.getId(), "status", POMPOM.STATUS_COMPLETED);
			editList.add(command);
		}

		MultiEditCommand multiEdit = new MultiEditCommand(editList);
		multiEdit.execute();

		pompom.refreshStatus();
		/**
		 * check if the statuses remain completed as edit command was used
		 */
		assertEquals(POMPOM.STATUS_COMPLETED, firstTask.getStatus());
		assertEquals(POMPOM.STATUS_COMPLETED, secondTask.getStatus());
//		assertEquals(POMPOM.STATUS_COMPLETED, thirdTask.getStatus());

	}

}
```
