package gui;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import main.POMPOM;

public class SettingsController {
	@FXML
	Button saveFile;
	@FXML
	Button selectFile;
	@FXML
	TextField storageLocationString;
	
    public void clickSave(ActionEvent event) throws IOException {
    	String storageFilePath = storageLocationString.getText();
        POMPOM.saveSettings(storageFilePath);
    }  
	

	public void showSingleFileChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser(); 
		File selectedPath = directoryChooser.showDialog(null);
		storageLocationString.setText(selectedPath.getPath());
	}

}
