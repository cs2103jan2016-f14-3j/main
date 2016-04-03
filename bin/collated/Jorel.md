# Jorel
###### gui\Controller.java
``` java
 *
 */
public class Controller implements Initializable {
	@FXML
	Pane mainContent;
	@FXML
	Pane content;
	@FXML
	ImageView settings;
	@FXML
	Button settingBtn;
	@FXML
	Button newTask;
	@FXML
	Button editTask;
	@FXML
	Button saveTask;
	@FXML
	Button deleteTask;
	@FXML
	Button enterCommand;
	@FXML
	TableView<Item> table;
	@FXML
	TableColumn<Item, Number> taskID;
	@FXML
	TableColumn<Item, String> taskName;
	@FXML
	TableColumn<Item, Boolean> checkBox;

	@FXML
	TableColumn<Item, Date> taskStartDateTime;
	@FXML
	TableColumn<Item, Date> taskEndDateTime;
	@FXML
	TableColumn<Item, String> taskLabel;
	@FXML
	TableColumn<Item, String> taskPriority;
	@FXML
	TableColumn<Item, String> taskStatus;
	@FXML
	ListView<String> list;
	@FXML
	TextField inputCommand;
	@FXML
	Label returnMsg;
	@FXML
	Label label;
	@FXML
	TabPane tabViews;
	@FXML
	private Tab taskTab;
	@FXML
	private Tab eventTab;
	@FXML
	private Tab searchTab;

	String msg;
	Stage stage;
	private Main main = new Main();
	private boolean initialized = false;

	ArrayList<Item> displayList;

	Node node;

	ObservableList<String> taskView = FXCollections.observableArrayList();
	static ObservableList<Item> tableContent;
	POMPOM pompom;

	public static ObservableList<Item> getTableContent() {
		return tableContent;
	}

	public ArrayList<Item> getDisplayList() {
		return displayList;
	}

	public void setDisplayList(ArrayList<Item> displayList) {
		this.displayList = displayList;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert newTask != null : "fx:id=\"newTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert editTask != null : "fx:id=\"editTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert saveTask != null : "fx:id=\"saveTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert deleteTask != null : "fx:id=\"deleteTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert enterCommand != null : "fx:id=\"enterCommand\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert checkBox != null : "fx:id=\"checkBox\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert taskID != null : "fx:id=\"taskID\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert taskName != null : "fx:id=\"taskName\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert taskStartDateTime != null : "fx:id=\"taskDateTime\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert taskEndDateTime != null : "fx:id=\"taskDateTime\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert taskLabel != null : "fx:id=\"taskLabel\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert taskPriority != null : "fx:id=\"taskPriority\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert taskStatus != null : "fx:id=\"taskStatus\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert inputCommand != null : "fx:id=\"inputCommand\" was not injected: check your FXML file 'POMPOM.fxml'.";
		// Initialize main logic class
		pompom = new POMPOM();

		System.out.println(this.getClass().getSimpleName() + ".initialize");
		// Initialize on task tab
		POMPOM.setCurrentTab(POMPOM.LABEL_TASK);
		displayList = ListClassifier.getTaskList(POMPOM.getStorage()
				.getTaskList());
		configureButtons();
		configureTable();
		initialized = true;

	}

	private void configureButtons() {
		if (newTask != null) {
			newTask.setDisable(false);
		}
		if (saveTask != null) {
			saveTask.setDisable(true);
		}
		if (editTask != null) {
			editTask.setDisable(true);
		}
		if (deleteTask != null) {
			deleteTask.setDisable(false);
		}
		if (enterCommand != null) {
			enterCommand.setDisable(false);
		}
	}

	@FXML
	private void deleteItems() {
		int rows = table.getItems().size();
		System.out.println("arara: " + checkBox.getCellData(0));
		for (int i = 0; i < rows; i++) {
			if (checkBox.getCellData(i) == true) {
				// Change to logger
				System.out.println("ID Deleted: "
						+ Long.parseLong(taskID.getCellData(i).toString()));
				DelCommand delCommand = new DelCommand(Long.parseLong(taskID
						.getCellData(i).toString()));
				POMPOM.executeCommand(delCommand);
			}
		}
		switchToTab(POMPOM.getCurrentTab().toLowerCase());
	}

	void configureTable() {
		table.setEditable(true);
		taskID.setCellValueFactory(new PropertyValueFactory<Item, Number>("id"));
		taskName.setCellValueFactory(new PropertyValueFactory<Item, String>(
				"title"));

		//Checkbox init
		checkBox.setCellFactory(CheckBoxTableCell.forTableColumn(checkBox));
		checkBox.setCellValueFactory(c -> c.getValue().checkedProperty());
		
		taskStartDateTime
				.setCellValueFactory(new PropertyValueFactory<Item, Date>(
						"startDate"));
		taskEndDateTime
				.setCellValueFactory(new PropertyValueFactory<Item, Date>(
						"endDate"));
		taskLabel.setCellValueFactory(new PropertyValueFactory<Item, String>(
				"label"));
		taskPriority
				.setCellValueFactory(new PropertyValueFactory<Item, String>(
						"priority"));
		taskStatus.setCellValueFactory(new PropertyValueFactory<Item, String>(
				"status"));

		tableContent = FXCollections.observableArrayList(displayList);
		table.setItems(tableContent);
		table.refresh();

	}

	public void newTaskFired(ActionEvent event) {
		main.newTaskDialog();
	}

	public void toSettingsView(MouseEvent event) throws IOException {
		node = (Node) FXMLLoader.load(getClass().getResource("Settings.fxml"));
		content.getChildren().setAll(node);
	}

	public void toDashBoardView(MouseEvent event) throws IOException {
		node = (Node) FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
		content.getChildren().setAll(node);
	}

	// New methods by wei lip

	public void switchToTab(String inputTab) {
		String tabName = inputTab.toLowerCase();
		SingleSelectionModel<Tab> selectionModel = tabViews.getSelectionModel();
		
		if (tabName.equals(POMPOM.LABEL_TASK.toLowerCase())) {
			System.out.println("Teasdat :" + tabName);
			selectionModel.select(taskTab);
			taskTabAction();
		} else if (tabName.equals(POMPOM.LABEL_COMPLETED_TASK.toLowerCase())) {
			selectionModel.select(taskTab);
			completedTaskTabAction();
		} else if (tabName.equals(POMPOM.LABEL_EVENT.toLowerCase())) {
			System.out.println("evenets");
			selectionModel.select(eventTab);
			eventTabAction();
		} else if (tabName.equals(POMPOM.LABEL_COMPLETED_EVENT.toLowerCase())) {
			selectionModel.select(eventTab);
			completedEventTabAction();
		} else if (tabName.equals(POMPOM.LABEL_SEARCH.toLowerCase())) {
			System.out.println("SEARCH TESET");
			selectionModel.select(searchTab);
			searchTabAction();
		}
	}
	@FXML
	public void taskTabAction() {
		if (!initialized)
			return;

		displayList = ListClassifier.getTaskList(POMPOM.getStorage()
				.getTaskList());
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_TASK.toLowerCase());
	}
	@FXML
	public void completedTaskTabAction() {
		if (!initialized) {
			return;
		}
		displayList = ListClassifier.getDoneTaskList(POMPOM.getStorage()
				.getTaskList());
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_COMPLETED_TASK.toLowerCase());
	}
	@FXML
	public void eventTabAction() {
		System.out.println("cleared");
		displayList = ListClassifier.getEventList(POMPOM.getStorage()
				.getTaskList());
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_EVENT.toLowerCase());
		System.out.println("eventtabaction: " + POMPOM.getCurrentTab());
	}
	@FXML	
	public void completedEventTabAction() {
		displayList = ListClassifier.getDoneEventList(POMPOM.getStorage()
				.getTaskList());
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_COMPLETED_EVENT.toLowerCase());
	}

	public void searchTabAction() {
		if (POMPOM.getSearchList() != null) {
			displayList = POMPOM.getSearchList();
		}
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_SEARCH.toLowerCase());
	}

	// ////////// Wei LIP ////////////

	public void enterCommandFired(ActionEvent event) throws IOException {
		Timeline timeline = new Timeline();
		String input = inputCommand.getText();
		inputCommand.clear();
		msg = pompom.execute(input);
		switchToTab(POMPOM.getCurrentTab().toLowerCase());

		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(0), new KeyValue(returnMsg
						.textProperty(), msg)));
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(2), new KeyValue(returnMsg
						.textProperty(), " ")));
		timeline.play();
		configureTable();
		POMPOM.getStorage().saveStorage();
		inputCommand.setPromptText("Command:");
	}

	public void enterCommandKey(KeyEvent event) throws IOException {
		Timeline timeline = new Timeline();
		if (event.getCode().equals(KeyCode.ENTER)) {
			String input = inputCommand.getText();
			msg = pompom.execute(input);
			switchToTab(POMPOM.getCurrentTab().toLowerCase());
			timeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(2), new KeyValue(returnMsg
							.textProperty(), msg)));
			timeline.play();
			returnMsg.setText("");
			POMPOM.getStorage().saveStorage();
			inputCommand.clear();
			inputCommand.setPromptText("Command:");

		}

	}

}
```
###### gui\Main.java
``` java
 *
 */
public class Main extends Application {
	
	
	@FXML
	Pane content;
	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		getClass();
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("POMPOM.fxml"));
		Parent root = FXMLLoader.load(getClass().getResource("POMPOM.fxml"));
		stage.setTitle("POMPOM");
		stage.setScene(new Scene(root, 800, 556));
		
		stage.show();
	}

	public boolean newTaskDialog() {
		try {
			FXMLLoader loader = new FXMLLoader(
					Main.class.getResource("NewTask.fxml"));
			Pane page = (Pane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Task");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			NewTaskController newTaskController = loader.getController();
			newTaskController.setDialogStage(dialogStage);

			dialogStage.showAndWait();

			return newTaskController.isOkClicked();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
```
###### gui\NewTaskController.java
``` java
 *
 */
public class NewTaskController implements Initializable{

	@FXML
	TextField newTaskTitle;
	@FXML
	DatePicker newStartDate;
	@FXML
	TextField newStartTime;
	@FXML
	TextField newLabel;
	@FXML
	DatePicker newEndDate;
	@FXML
	TextField newEndTime;
	@FXML
	SplitMenuButton newPriority;
	@FXML
	MenuItem highSelected;
	@FXML
	MenuItem mediumSelected;
	@FXML
	MenuItem lowSelected;
	@FXML
	Button newTaskSave;
	@FXML
	Button newTaskCancel;
	@FXML
	TextField newType;
	
	private Stage dialogStage;
	Controller control = new Controller();
    POMPOM pompom = new POMPOM();
	private Controller controller;
	private boolean okClicked = false;
    String title;
    LocalDate startDate;
    String startTime;
    LocalDate endDate;
    String endTime;
    String label;
    String priority;
    String type;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        assert newTaskTitle != null : "fx:id=\"newTaskTitle\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert newStartDate != null : "fx:id=\"newDate\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert newStartTime != null : "fx:id=\"newTime\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert newLabel != null : "fx:id=\"newLabel\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert newPriority != null : "fx:id=\"newPriority\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert highSelected != null : "fx:id=\"highSelected\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert mediumSelected != null : "fx:id=\"mediumSelected\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert lowSelected != null : "fx:id=\"lowSelected\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert newTaskCancel != null : "fx:id=\"newTaskCancel\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert newTaskSave != null : "fx:id=\"newTaskSave\" was not injected: check your FXML file 'POMPOM.fxml'.";
        System.out.println(this.getClass().getSimpleName() + ".initialize");
	}
	
	public void setDialogStage(Stage dialogStage) {
		
		this.dialogStage = dialogStage;					
	}	
	
//	1. add <task>
//	2. add <task> <mmm dd>
//	3. add <task> <dd/mm/yyyy>
//	4. add <task> <f:mmm dd> <mmm dd>
//	5. add <task> <f:dd/mm/yyyy> <mmm dd>
//	6. add <task> <f:mmm dd> <dd/mm/yyyy>
//	7. add <task> <today/tomorrow/this week/month/year/ next week/month/year>
//	8. add <task> <today/tomorrow/this week/month/year/ next week/month/year> <f:today/tomorrow/this week/month/year/ next week/month/year>
//	9. add <task> <dd/mm/yyyy> <f:today/tomorrow/this week/month/year/ next week/month/year>
//	10. add <task> <dd mmm> <f:today/tomorrow/this week/month/year/ next week/month/year>
//	11. add <task> <today/tomorrow/this week/month/year/ next week/month/year><f:dd mmm>
//	12.add <task> <today/tomorrow/this week/month/year/ next week/month/year><f:dd/mm/yyyy>
	@FXML
	private void handleSave() throws IOException, ParseException{
		controller = new Controller();
		title = newTaskTitle.getText();
		startDate = newStartDate.getValue();
		startTime = newStartTime.getText();
		endDate = newEndDate.getValue();
		endTime = newEndTime.getText();
		label = newLabel.getText();
		priority = newPriority.getText();
		type = newType.getText();
		System.out.println(startDate);
		
		Instant instantSD = startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date sDate = Date.from(instantSD);
		
		Instant instantED = startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date eDate = Date.from(instantED);
		
		AddCommand addCommand = new AddCommand(type, title, "", "", "", label, sDate, eDate);
		POMPOM.executeCommand(addCommand);
		
//		String input = "add " + "f:";
//		String input = "add " + title + " null " + priority + " Upcoming " + label  + " " + startDate + "/" + startTime + " " + endDate + "/" + endTime;
//		System.out.println(input);
//		pompom.execute(input);
		okClicked = true;
		
		POMPOM.getStorage().saveStorage();
		dialogStage.close();
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public boolean isOkClicked() {
		return false;
	}
	



	
}
```
###### gui\SettingsController.java
``` java
 **
 */
public class SettingsController {
	@FXML
	Button saveFile;
	@FXML
	Button selectFile;
	@FXML
	TextField storageLocationString;
	
    public void clickSave(ActionEvent event) throws IOException {
    	String storageFilePath = storageLocationString.getText();
    	storageLocationString.clear();
        POMPOM.saveSettings(storageFilePath);
    }  
	

	public void showSingleFileChooser() {
		FileChooser fileChooser = new FileChooser(); 
		File selectedPath = fileChooser.showOpenDialog(null);
		storageLocationString.setText(selectedPath.getPath());
	}

}
```
###### Test\TestSomething.java
``` java
 *
 */
public class TestSomething {
	/**
	 * This operation test return responds for correct input task add
	 */
	@Test
	public void test() {
		POMPOM pompom = new POMPOM();
		assertEquals("Task added", pompom.execute("add hello"));
	}
	
	/**
	 * This operation test return responds for invalid input
	 */
	@Test
	public void testTwo() {
		POMPOM pompom = new POMPOM();
		assertEquals("hello", pompom.execute("hello"));
	}

}
```
