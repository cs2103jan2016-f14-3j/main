package gui;

import java.net.URL;
import java.time.LocalDate;
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
import utils.Item;

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
	private Item item;
	private boolean okClicked = false;
    POMPOM pompom = new POMPOM();
    String title;
    LocalDate startDate;
    String formattedStartDate;
    String startTime;
    LocalDate endDate;
    String formattedEndDate;
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

	public void setItem(Item item) {
		this.item = item;
		
		title = newTaskTitle.getText();
		startDate = newStartDate.getValue();
		startTime = newStartTime.getText();
		endDate = newEndDate.getValue();
		formattedEndDate = endDate.toString();
		endTime = newEndTime.getText();
		label = newLabel.getText();
		priority = newPriority.getText();
		System.out.println(title);
	}

	@FXML
	private void handleSave(){
		setItem(new Item());
		String input = "add " + title + " null " + priority + " Upcoming " + label  + " " + formattedStartDate + "/" + startTime + " " + formattedEndDate + "/" + endTime;
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
