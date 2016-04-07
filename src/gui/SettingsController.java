package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import utils.Settings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import main.POMPOM;

/**
 * @@author Jorel
 **
 */
public class SettingsController implements Initializable {
	@FXML
	Button saveFile;
	@FXML
	Button selectFile;
	@FXML
	TextField storageLocationString;
	@FXML
	ColorPicker backgroundColour;
	@FXML
	ColorPicker tabColour;
	@FXML
	ColorPicker buttonColour;
	@FXML
	ColorPicker oddCellColour;
	@FXML
	ColorPicker evenCellColour;
	
	Settings currentSettings;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert saveFile != null : "fx:id=\"saveFile\" was not injected: check your FXML file 'Settings.fxml'.";
		assert selectFile != null : "fx:id=\"selectFile\" was not injected: check your FXML file 'Settings.fxml'.";
		assert storageLocationString != null : "fx:id=\"storageLocationString\" was not injected: check your FXML file 'Settings.fxml'.";
		assert backgroundColour != null : "fx:id=\"backgroundColour\" was not injected: check your FXML file 'Settings.fxml'.";
		assert tabColour != null : "fx:id=\"tabColour\" was not injected: check your FXML file 'Settings.fxml'.";
		assert buttonColour != null : "fx:id=\"buttonColour\" was not injected: check your FXML file 'Settings.fxml'.";
		assert oddCellColour != null : "fx:id=\"oddCellColour\" was not injected: check your FXML file 'Settings.fxml'.";
		assert evenCellColour != null : "fx:id=\"evenCellColour\" was not injected: check your FXML file 'Settings.fxml'.";
		init();
	}
	//Set the current display colours if not set to white
	public void init() {
		currentSettings = GUIModel.getSettings();
		backgroundColour.setValue(Color.valueOf(currentSettings.getBackgroundColour())); 
		tabColour.setValue(Color.valueOf(currentSettings.getTabColour()));
		buttonColour.setValue(Color.valueOf(currentSettings.getButtonColour()));
		oddCellColour.setValue(Color.valueOf(currentSettings.getOddCellColour()));
		evenCellColour.setValue(Color.valueOf(currentSettings.getEvenCellColour()));
	}
	public String getColorString(ColorPicker picker){
		String hex = picker.getValue().toString();
		String color = hex.substring(2, hex.length() -2);
		return color;
	} 
	
    public void clickSave(ActionEvent event) throws IOException {
    	String storageFilePath = storageLocationString.getText(); 
//    	storageLocationString.clear();
    	
    	
    	currentSettings.setBackgroundColour(getColorString(backgroundColour));
    	currentSettings.setTabColour(getColorString(tabColour));
    	currentSettings.setButtonColour(getColorString(buttonColour));
    	currentSettings.setEvenCellColour(getColorString(evenCellColour));
    	currentSettings.setOddCellColour(getColorString(oddCellColour));
    	
        POMPOM.saveSettings(storageFilePath);
    }  
	
    

	public void showSingleFileChooser() {
		FileChooser fileChooser = new FileChooser(); 
		File selectedPath = fileChooser.showOpenDialog(null);
		storageLocationString.setText(selectedPath.getPath());
	}

}
