package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import command.AddCommand;
import command.EditCommand;
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
import utils.Item;
import gui.Controller;

/**
 * @@author Jorel
 *
 */
public class EditTaskController implements Initializable{

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
    Long taskId;
    boolean okClicked;
    
    ObservableList<String> options = FXCollections.observableArrayList("High", "Medium", "Low");
	ObservableList<String> itemType = FXCollections.observableArrayList("Task", "Event");

    private Item item;
    
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
	}
	
	public void setDialogStage(Stage dialogStage) {
		
		this.dialogStage = dialogStage;					
	}
	
	public void setItem(Item item) {
		this.item = item;
		newTaskTitle.setText(item.getTitle());	
		newLabel.setText(item.getLabel());
		newPriority.setValue(item.getPriority());
		newType.setValue(item.getType());
		taskId = item.getId();
		Date retrieveSDate = item.getStartDate();
	    Calendar cal = Calendar.getInstance();
	    int year;
	    int month;
	    int day;
	    int hour;
	    int min;
		if(retrieveSDate != null){
	    cal.setTime(retrieveSDate);
	    year = cal.get(Calendar.YEAR);
	    month = cal.get(Calendar.MONTH);
	    day = cal.get(Calendar.DAY_OF_MONTH);
	    hour = cal.get(Calendar.HOUR_OF_DAY);
	    min = cal.get(Calendar.MINUTE);
		newStartDate.setValue(LocalDate.of(year, month+1, day));
		newStartHour.setText(Integer.toString(hour));
		newStartMin.setText(Integer.toString(min));
		}
		Date retrieveEDate = item.getEndDate();
		if(retrieveEDate!=null){
		cal.setTime(retrieveEDate);
	    year = cal.get(Calendar.YEAR);
	    month = cal.get(Calendar.MONTH);
	    day = cal.get(Calendar.DAY_OF_MONTH);
	    hour = cal.get(Calendar.HOUR_OF_DAY);
	    min = cal.get(Calendar.MINUTE);
	    newEndDate.setValue(LocalDate.of(year, month+1, day));
	    newEndHour.setText(Integer.toString(hour));
	    newEndMin.setText(Integer.toString(min));
		}
	}

			
	@FXML
	private void handleSave() throws IOException, ParseException{
		title = newTaskTitle.getText();
		startDate = newStartDate.getValue();
		startHour = newStartHour.getText();
		startMin = newStartMin.getText();
		endDate = newEndDate.getValue();
		endHour= newEndHour.getText();
		endMin = newEndMin.getText();
		label = newLabel.getText();
		priority = newPriority.getValue();
		type = newType.getValue();
		
	      Calendar sc =  Calendar.getInstance();
	      sc.set(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
	      Date sd = sc.getTime();
	      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	      String sDateTime = sdf.format(sd) + " " + startHour + ":" + startMin;
	      Date sDate = dateFormat.parse(sDateTime);
		
	      Calendar ec =  Calendar.getInstance();
	      ec.set(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth());
	      Date ed = ec.getTime();
	      String eDateTime = sdf.format(ed) + " " + endHour + ":" + endMin;
	      Date eDate = dateFormat.parse(eDateTime);

		EditCommand editTitle = new EditCommand(taskId, "title", title);
		EditCommand editPriority = new EditCommand(taskId, "priority", priority);
		EditCommand editLabel= new EditCommand(taskId, "label", label);
		EditCommand editStatus = new EditCommand(taskId, "type", type);
		EditCommand editStartDate = new EditCommand(taskId, "start date", sDate);
		EditCommand editEndDate = new EditCommand(taskId, "end date", eDate);
		
		POMPOM.executeCommand(editTitle);
		POMPOM.executeCommand(editPriority);
		POMPOM.executeCommand(editLabel);
		POMPOM.executeCommand(editStatus);
		POMPOM.executeCommand(editStartDate);
		POMPOM.executeCommand(editEndDate);
		
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
