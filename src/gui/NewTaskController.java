package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.POMPOM;
import gui.Controller;
import utils.Item;
import gui.Controller;

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
		String input = "add " + title + " null " + priority + " Upcoming " + label  + " " + startDate + "/" + startTime + " " + endDate + "/" + endTime;
		System.out.println(input);
		pompom.execute(input);
		okClicked = true;
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
