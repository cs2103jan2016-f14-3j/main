package gui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.POMPOM;
import utils.Item;
import utils.ListClassifier;
import utils.Settings;

import command.DelCommand;

/**
 * @@author Jorel
 *
 */

public class MainController implements Initializable {
	// Pane items in main display
	@FXML
	Pane mainContent;
	@FXML
	Pane content;
	@FXML
	Button settings;
	@FXML
	Button settingBtn;
	@FXML
	Button highPBtn;
	@FXML
	Button mediumPBtn;
	@FXML
	Button newTask;
	@FXML
	Button editTask;
	@FXML
	Button deleteTask;
	@FXML
	Button help;
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
	TableColumn<Item, String> taskStartDateTime;
	@FXML
	TableColumn<Item, String> taskEndDateTime;
	@FXML
	TableColumn<Item, String> taskLabel;
	@FXML
	TableColumn<Item, String> taskPriority;
	@FXML
	TableColumn<Item, String> taskStatus;
	@FXML
	TextField inputCommand;
	@FXML
	Label returnMsg;
	@FXML
	Label label;
	@FXML
	Label priorityLabel;
	@FXML
	TabPane tabViews;
	@FXML
	private Tab taskTab;
	@FXML
	private Tab completedTaskTab;
	@FXML
	private Tab eventTab;
	@FXML
	private Tab completedEventTab;
	@FXML
	private Tab searchTab;

	// DASHBOARD ITEMS
	@FXML
	Label dashboardLbl;
	@FXML
	Label settingLbl;
	@FXML
	Label lowPriorityLbl;
	@FXML
	Label highPriorityLbl;
	@FXML
	Label mediumPriorityLbl;
	@FXML
	Button dashBoard;
	@FXML
	Label taskNo;
	@FXML
	Label overdueNo;
	@FXML
	Label eventsNo;
	@FXML
	Pane mainPane;
	//Content
	Stage stage;
	private Main main = new Main();
	private boolean initialized = false;	
	ObservableList<Item> displayList;
	ObservableList<String> taskView = FXCollections.observableArrayList();
	ObservableList<Item> tableContent;
	POMPOM pompom;
	CheckBox selectAll = new CheckBox();
	
	// String variables
	public static final String DEFAULT_LABEL_COLOR = "#ffffff";
	public static final String DEFAULT_HIGHLIGHT_COLOR = "#7d8993";
	public static final String CSS_STYLE_TEXT = "-fx-text-fill: #";
	


	public ObservableList<Item> getTableContent() {
		return tableContent;
	}

	public ObservableList<Item> getDisplayList() {
		return displayList;
	}

	public void setDisplayList(ObservableList<Item> displayList) {
		this.displayList = displayList;
	}

	public void formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date d = new Date();
		dateFormat.format(d);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert newTask != null : "fx:id=\"newTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert editTask != null : "fx:id=\"editTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
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
		GUIModel.update();
		System.out.println(this.getClass().getSimpleName() + ".initialize");

		// Initialize the GUI
		POMPOM.setCurrentTab(POMPOM.LABEL_TASK);
		displayList = GUIModel.getTaskList();		
		configureButtons();
		configureTable();
		setNotificationLabels();
		//Set focus to command text striaght
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				inputCommand.requestFocus();
			}
		});
		setSettingsColours();
		initialized = true;		

	}
	void configureTable() {
		table.setEditable(true);

		taskID.setCellValueFactory(new PropertyValueFactory<Item, Number>("id"));
		taskName.setCellValueFactory(new PropertyValueFactory<Item, String>(
				"title"));

		// Checkbox init
		checkBox.setGraphic(selectAll);

		checkBox.setCellFactory(CheckBoxTableCell.forTableColumn(checkBox));
		checkBox.setCellValueFactory(c -> c.getValue().checkedProperty());
		taskStartDateTime
				.setCellValueFactory(new PropertyValueFactory<Item, String>(
						"sd"));
		taskEndDateTime
				.setCellValueFactory(new PropertyValueFactory<Item, String>(
						"ed"));
		taskLabel.setCellValueFactory(new PropertyValueFactory<Item, String>(
				"label"));
		taskPriority
				.setCellValueFactory(new PropertyValueFactory<Item, String>(
						"priority"));
		taskStatus.setCellValueFactory(new PropertyValueFactory<Item, String>(
				"status"));

		GUIModel.update();

		selectAll(selectAll);

		tableContent = FXCollections.observableArrayList(displayList);
		table.setItems(displayList);

		table.refresh();
		inputCommand.requestFocus();

	}





	@FXML
	private void deleteItems() throws IOException {
		int rows = table.getItems().size();
		for (int i = 0; i < rows; i++) {
			if (checkBox.getCellData(i) == true) {
				
				DelCommand delCommand = new DelCommand(Long.parseLong(taskID
						.getCellData(i).toString()));
				POMPOM.executeCommand(delCommand);
				configureTable();
				POMPOM.getStorage().saveStorage();
			}
		}
		switchToTab(POMPOM.getCurrentTab().toLowerCase());
	}

	@FXML
	private void editItem() throws IOException {
		Item item = table.getSelectionModel().getSelectedItem();
		if (item != null) {
			Stage dialogStage = main.editTaskDialog(item, this);

		}
	}

	public void enableEditBtn(MouseEvent event) {
		if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
			editTask.setDisable(false);
		}
	}



	public void newTaskFired(ActionEvent event) throws IOException {
		Stage dialogStage = main.newTaskDialog(this);
		System.out.println(dialogStage);

	}

	public void helpFired(ActionEvent event) {
		main.helpDialog();
	}
	

	public void changeToSettings(ActionEvent event) throws IOException {

		highLightLabel(settingLbl);
		
		content.getChildren().clear();
		Node node = (Node) FXMLLoader.load(getClass().getResource("Settings.fxml"));
		content.getChildren().setAll(node);
	}

	
	public void changeToDashboard(ActionEvent event) throws IOException {

		highLightLabel(dashboardLbl);

		Node node = (Node) FXMLLoader.load(getClass().getResource("POMPOM.fxml"));
		mainPane.getChildren().setAll(node);

		// content.getChildren().setAll(node);

	}
	
	
	@FXML
	public void changeToHighPriority(ActionEvent event) {

		highLightLabel(highPriorityLbl);
		GUIModel.update();

		// Filter
		switchToTab(GUIModel.getCurrentTab());
		for (Item item : displayList) {
			System.out.println(item.getTitle());
		}
		displayList = ListClassifier
				.getSpecifiedPrirorirty(displayList, "high");
		configureTable();
	}

	
	@FXML
	public void changeToMediumPriority(ActionEvent event) {
		highLightLabel(mediumPriorityLbl);
		// Filter
		switchToTab(GUIModel.getCurrentTab());
		displayList = ListClassifier.getSpecifiedPrirorirty(displayList,
				"medium");

		configureTable();
	}

	
	@FXML
	public void changeToLowPriority(ActionEvent event) {
		highLightLabel(lowPriorityLbl);
		// Filter
		switchToTab(GUIModel.getCurrentTab());
		displayList = ListClassifier.getSpecifiedPrirorirty(displayList, "low");

		configureTable();
	}


	/**
	 * Select inputTab 
	 * @param inputTab
	 */
	public void switchToTab(String inputTab) {
		String tabName = inputTab.toLowerCase();
		SingleSelectionModel<Tab> selectionModel = tabViews.getSelectionModel();

		if (tabName.equals(POMPOM.LABEL_TASK.toLowerCase())) {
			selectionModel.select(taskTab);
			taskTabAction();
		} else if (tabName.equals(POMPOM.LABEL_COMPLETED_TASK.toLowerCase())) {
			selectionModel.select(completedTaskTab);
			completedTaskTabAction();
		} else if (tabName.equals(POMPOM.LABEL_EVENT.toLowerCase()	)) {
			selectionModel.select(eventTab);
			eventTabAction();
		} else if (tabName.equals(POMPOM.LABEL_COMPLETED_EVENT.toLowerCase())) {
			selectionModel.select(completedEventTab);
			completedEventTabAction();
		} else if (tabName.equals(POMPOM.LABEL_SEARCH.toLowerCase())) {
			selectionModel.select(searchTab);
			searchTabAction();
		}
	}

	@FXML
	public void taskTabAction() {
		if (!initialized)
			return;
		GUIModel.update();
		displayList = GUIModel.getTaskList();
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_TASK.toLowerCase());
	}

	@FXML
	public void completedTaskTabAction() {
		GUIModel.update();
		displayList = GUIModel.getTaskDoneList();
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_COMPLETED_TASK.toLowerCase());
	}

	@FXML
	public void eventTabAction() {
		GUIModel.update();
		displayList = GUIModel.getEventList();
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_EVENT.toLowerCase());
	}

	@FXML
	public void completedEventTabAction() {
		GUIModel.update();
		displayList = GUIModel.getEventDoneList();
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_COMPLETED_EVENT.toLowerCase());
	}
	
	public void searchTabAction() {
		if (POMPOM.getSearchList() != null) {
			displayList = FXCollections.observableArrayList(POMPOM
					.getSearchList());
		}
		configureTable();
		POMPOM.setCurrentTab(POMPOM.LABEL_SEARCH.toLowerCase());
	}

	/**
	 * Command triggered when enter button is pressed
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void enterCommandFired(ActionEvent event) throws IOException {
		//Clear input string
		String input = inputCommand.getText();
		inputCommand.clear();
		//Execute command
		String msg = pompom.execute(input);
		switchToTab(POMPOM.getCurrentTab().toLowerCase());
		POMPOM.getStorage().saveStorage();
		//Update Gui
		displayReturnMessage(msg);		
		configureTable();
		setNotificationLabels();		
		selectRow(input);
		inputCommand.setPromptText("Command:");
	}

	/**
	 * When you hit the enter key on the keyboard
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void enterCommandKey(KeyEvent event) throws IOException {
		if (event.getCode().equals(KeyCode.ENTER)) {
			//clear input string
			String input = inputCommand.getText();
			//Execute command
			String msg = pompom.execute(input);
			switchToTab(POMPOM.getCurrentTab().toLowerCase());			
			POMPOM.getStorage().saveStorage();
			
			//Update GUI
			displayReturnMessage(msg);
			configureTable();
			setNotificationLabels();			
			selectRow(input);
			inputCommand.clear();
			inputCommand.setPromptText("Command:");

		}

	}

	/***********************************Helper methods**********************************/
	
	private void configureButtons() {
		if (newTask != null) {
			newTask.setDisable(false);
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
	/**
	 * Highlight the item which has just been added or modified
	 * @param input
	 */
	public void selectRow(String input) {
		int i = input.indexOf(' ');
		if (i < 0)
			return;
		String command = input.substring(0, i);
		String restOfAction = input.substring(i + 1);
		int rowNo = table.getItems().size();
		if (command.equals("add") || command.equals("event")) {
			table.getSelectionModel().select(rowNo - 1);
			table.scrollTo(rowNo - 1);
		}
		if (command.equals("edit")) {
			int z;
			int j = restOfAction.indexOf(' ');
			int itemNo = Integer.parseInt(restOfAction.substring(0, j));
			for (z = 0; z < rowNo - 1; z++) {
				int searchItemNo = taskID.getCellData(z).intValue();
				if (itemNo == searchItemNo) {
					break;
				}
			}
			table.getSelectionModel().select(z);
			table.scrollTo(z);
		}
	}
	/**
	 * Select all for checkbox
	 */
	public void selectAll(CheckBox cb){
		cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {

				int rows = table.getItems().size();
				for (int i = 0; i < rows; i++) {
					boolean preVal = displayList.get(i).getChecked();

					displayList.get(i).setChecked(!preVal);
					
				}
			}
		});
	}

	private void setBackgroundColor(String color) {

		BackgroundFill myBF = new BackgroundFill(Color.valueOf(color),
				CornerRadii.EMPTY, Insets.EMPTY);
		content.setBackground(new Background(myBF));

	}

	/**
	 * Set the notification numbers in the labels
	 * Eg. Number of task today
	 */
	public void setNotificationLabels() {
		GUIModel.update();
		taskNo.setText(ListClassifier.getTodayTask(POMPOM.getStorage()
				.getTaskList()));
		overdueNo.setText(ListClassifier.getOverdueTask(POMPOM.getStorage()
				.getTaskList()));
		eventsNo.setText(ListClassifier.getTodayEvent(POMPOM.getStorage()
				.getTaskList()));
	}
	/**
	 * Displays Notification Message
	 */
	public void displayReturnMessage(String message){
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(0), new KeyValue(returnMsg
						.textProperty(), message)));
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(2), new KeyValue(returnMsg
						.textProperty(), " ")));
		timeline.play();
	}
	
	/**
	 * HighLight the clicked label
	 * @param label
	 */
	public void highLightLabel(Label label){
		dashboardLbl.setTextFill(Color.web(DEFAULT_LABEL_COLOR));
		settingLbl.setTextFill(Color.web(DEFAULT_LABEL_COLOR));
		highPriorityLbl.setTextFill(Color.web(DEFAULT_LABEL_COLOR));
		mediumPriorityLbl.setTextFill(Color.web(DEFAULT_LABEL_COLOR));
		lowPriorityLbl.setTextFill(Color.web(DEFAULT_LABEL_COLOR));
		
		label.setTextFill(Color.web(DEFAULT_HIGHLIGHT_COLOR));
	}
	/**
	 * Set the colours gotten from the settings text file
	 */
	public void setSettingsColours(){
		Settings setting = GUIModel.getSettings();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setBackgroundColor(setting.getBackgroundColour());
				returnMsg.setStyle(CSS_STYLE_TEXT
						+ setting.getReturnMsgColour());
				inputCommand.setStyle(CSS_STYLE_TEXT
						+ setting.getInputTxtColour());

			}
		});
	}

}
