package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import javafx.util.Duration;
import main.POMPOM;
import utils.Item;

public class Controller implements Initializable {

	@FXML
	ImageView dashBoard;
	@FXML
	ImageView calendar;
	@FXML
	ImageView settings;
	@FXML
	Pane content;
	
	@FXML
	Pane mainContent;
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
	TableColumn<Item, Boolean> checkBox;
	@FXML
	TableColumn<Item, Number> taskID;
	@FXML
	TableColumn<Item, String> taskName;
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

	String msg;
	Stage stage;
	private Main main = new Main();

	ArrayList<Item> temp;

	ObservableList<String> taskView = FXCollections.observableArrayList();
	static ObservableList<Item> tableContent;
	POMPOM pompom = new POMPOM();
	
	Node node;

	public static ObservableList<Item> getTableContent() {
		return tableContent;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert dashBoard != null : "fx:id=\"dashboard\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert calendar != null : "fx:id=\"calendar\" was not injected: check your FXML file 'POMPOM.fxml'.";	
		assert settings != null : "fx:id=\"settings\" was not injected: check your FXML file 'POMPOM.fxml'.";		
		assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'POMPOM.fxml'.";		
	
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

		System.out.println(this.getClass().getSimpleName() + ".initialize");

		configureButtons();
		configureTable();

	}
	
	public void toSettingsView(MouseEvent event) throws IOException {
		node = (Node) FXMLLoader.load(getClass().getResource("Settings.fxml"));
		content.getChildren().setAll(node);
	}

	public void toDashBoardView(MouseEvent event) throws IOException {
		node = (Node) FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
		content.getChildren().setAll(node);
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
			deleteTask.setDisable(true);
		}
		if (enterCommand != null) {
			enterCommand.setDisable(false);
		}
	}

	void configureTable() {
		table.setEditable(true);
		checkBox.setCellFactory(CheckBoxTableCell.forTableColumn(checkBox));
		taskID.setCellValueFactory(new PropertyValueFactory<Item, Number>("id"));
		taskName.setCellValueFactory(new PropertyValueFactory<Item, String>(
				"title"));
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

		temp = POMPOM.getStorage().getTaskList();
		tableContent = FXCollections.observableArrayList(temp);

		table.setItems(tableContent);
		table.refresh();

	}

	public void enterCommandFired(ActionEvent event) throws IOException {
		Timeline timeline = new Timeline();
		String input = inputCommand.getText();
		inputCommand.clear();
		msg = pompom.execute(input);
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
			timeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(0), new KeyValue(returnMsg
							.textProperty(), msg)));
			timeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(2), new KeyValue(returnMsg
							.textProperty(), " ")));
			timeline.play();
			configureTable();
			POMPOM.getStorage().saveStorage();
			inputCommand.clear();
			inputCommand.setPromptText("Command:");

		}

	}

	public void newTaskFired(ActionEvent event) {
		main.newTaskDialog();
	}
}
