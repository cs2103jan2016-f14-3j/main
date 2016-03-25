package gui;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import main.POMPOM;

/**
 * @@author Jorel
 **
 */
public class SettingsController {
	@FXML
	Button saveFile;
	@FXML
	Button selectFile;
	@FXML
	TextField storageLocationString;
	
    public void clickSave(ActionEvent event) throws IOException {
    	String storageFilePath = storageLocationString.getText();
    	storageLocationString.clear();
        POMPOM.saveSettings(storageFilePath);
    }  
	

	public void showSingleFileChooser() {
		FileChooser fileChooser = new FileChooser(); 
		File selectedPath = fileChooser.showOpenDialog(null);
		storageLocationString.setText(selectedPath.getPath());
	}

}
