package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import command.AddCommand;
import javafx.collections.FXCollections;
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
import utils.ListClassifier;
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
