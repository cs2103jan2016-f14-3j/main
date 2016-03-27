package gui;

import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import command.DelCommand;
import main.POMPOM;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import utils.Item;
import utils.ListClassifier;

/**
 * @@author Jorel
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
//		checkBox.setCellValueFactory(c -> c.getValue().checkedProperty());
//		
//		taskStartDateTime
//				.setCellValueFactory(new PropertyValueFactory<Item, Date>(
//						"startDate"));
//		taskEndDateTime
//				.setCellValueFactory(new PropertyValueFactory<Item, Date>(
//						"endDate"));
//		taskLabel.setCellValueFactory(new PropertyValueFactory<Item, String>(
//				"label"));
//		taskPriority
//				.setCellValueFactory(new PropertyValueFactory<Item, String>(
//						"priority"));
//		taskStatus.setCellValueFactory(new PropertyValueFactory<Item, String>(
//				"status"));

		tableContent = FXCollections.observableArrayList(displayList);
		table.setItems(tableContent);
		table.refresh();

	}

	public void newTaskFired(ActionEvent event) {
		main.newTaskDialog();
	}

	public void toSettingsView(MouseEvent event) throws IOException {
		content.getChildren().remove(node);
		node = (Node) FXMLLoader.load(getClass().getResource("Settings.fxml"));
		content.getChildren().setAll(node);
	}

	public void toDashBoardView(MouseEvent event) throws IOException {
		content.getChildren().remove(node);
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
