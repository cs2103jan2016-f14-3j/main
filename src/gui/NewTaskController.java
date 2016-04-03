package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import command.AddCommand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.POMPOM;
import gui.Controller;

/**
 * @@author Jorel
 *
 */
public class NewTaskController implements Initializable {

	@FXML
	TextField newTaskTitle;
	@FXML
	DatePicker newStartDate;
	@FXML
	TextField newStartHour;
	@FXML
	TextField newStartMin;
	@FXML
	TextField newLabel;
	@FXML
	DatePicker newEndDate;
	@FXML
	TextField newEndHour;
	@FXML
	TextField newEndMin;
	@FXML
	ComboBox<String> newPriority;
	@FXML
	Button newTaskSave;
	@FXML
	Button newTaskCancel;
	@FXML
	ComboBox<String> newType;

	private Stage dialogStage;
	Controller control = new Controller();
	POMPOM pompom = new POMPOM();
	String title;
	LocalDate startDate;
	String startHour;
	String startMin;
	LocalDate endDate;
	String endHour;
	String endMin;
	String label;
	String priority;
	String type;

	boolean okClicked;

	ObservableList<String> options = FXCollections.observableArrayList("High", "Medium", "Low");
	ObservableList<String> itemType = FXCollections.observableArrayList("Task", "Event");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert newTaskTitle != null : "fx:id=\"newTaskTitle\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert newStartDate != null : "fx:id=\"newDate\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert newStartHour != null : "fx:id=\"newStartHour\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert newStartMin != null : "fx:id=\"newStartMin\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert newLabel != null : "fx:id=\"newLabel\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert newPriority != null : "fx:id=\"newPriority\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert newTaskCancel != null : "fx:id=\"newTaskCancel\" was not injected: check your FXML file 'POMPOM.fxml'.";
		assert newTaskSave != null : "fx:id=\"newTaskSave\" was not injected: check your FXML file 'POMPOM.fxml'.";
		System.out.println(this.getClass().getSimpleName() + ".initialize");
		newPriority.setItems(options);
		newType.setItems(itemType);
		newType.setValue("Task");
	}

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;
	}


	@FXML
	private void handleSave() throws IOException, ParseException {
		title = newTaskTitle.getText();
		startDate = newStartDate.getValue();
		startHour = newStartHour.getText();
		startMin = newStartMin.getText();
		endDate = newEndDate.getValue();
		endHour = newEndHour.getText();
		endMin = newEndMin.getText();
		label = newLabel.getText();
		priority = newPriority.getValue();
		type = newType.getValue();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Calendar sc = Calendar.getInstance();
		sc.set(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
		Date sd = sc.getTime();
		String sDateTime = sdf.format(sd) + " " + startHour + ":" + startMin;
		Date sDate = dateFormat.parse(sDateTime);

		Calendar ec = Calendar.getInstance();
		ec.set(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth());
		Date ed = ec.getTime();
		String eDateTime = sdf.format(ed) + " " + endHour + ":" + endMin;
		Date eDate = dateFormat.parse(eDateTime);

		AddCommand addCommand = new AddCommand(type, title, "", priority, "", label, sDate, eDate);
		POMPOM.executeCommand(addCommand);

		okClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public boolean isOkClicked() {
		return okClicked;
	}

}
