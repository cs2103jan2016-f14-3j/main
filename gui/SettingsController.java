package gui;

import java.io.IOException;

import command.PathCommand;

import main.POMPOM;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class SettingsController {
	@FXML
	Button save;
	@FXML
	TextField storageLocationString;
	
    public void clickSave(ActionEvent event) throws IOException {
    	String storageFilePath = storageLocationString.getText();
    	PathCommand pathCommand = new PathCommand(storageFilePath);
    	String msgReturned = POMPOM.executeCommand(pathCommand);
//      POMPOM.saveSettings(storageFilePath);
    }  
	
}
