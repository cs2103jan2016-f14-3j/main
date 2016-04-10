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
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
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
	ColorPicker displayMsgColor;
	@FXML
	ColorPicker commandTextColor;
	@FXML
	Pane mainPane;
	@FXML
	Label displayMsg;
	
	Settings currentSettings;
	public static final String RETURN_MSG = "Settings Saved";
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert saveFile != null : "fx:id=\"saveFile\" was not injected: check your FXML file 'Settings.fxml'.";
		assert selectFile != null : "fx:id=\"selectFile\" was not injected: check your FXML file 'Settings.fxml'.";
		assert storageLocationString != null : "fx:id=\"storageLocationString\" was not injected: check your FXML file 'Settings.fxml'.";
		assert backgroundColour != null : "fx:id=\"backgroundColour\" was not injected: check your FXML file 'Settings.fxml'.";
		assert displayMsgColor != null : "fx:id=\"tabColour\" was not injected: check your FXML file 'Settings.fxml'.";
		
		init();
	}
	//Set the current display colours if not set to white
	public void init() {
		currentSettings = GUIModel.getSettings();
		storageLocationString.setText(currentSettings.getStoragePath());
		backgroundColour.setValue(Color.valueOf(currentSettings.getBackgroundColour())); 
		displayMsgColor.setValue(Color.valueOf(currentSettings.getReturnMsgColour()));
		commandTextColor.setValue(Color.valueOf(currentSettings.getInputTxtColour()));
	}
	public String getColorString(ColorPicker picker){
		String hex = picker.getValue().toString();
		String color = hex.substring(2, hex.length() -2);
		return color;
	} 
	
    public void clickSave(ActionEvent event) throws IOException {
    	String storageFilePath = storageLocationString.getText(); 
//    	storageLocationString.clear();
    	
    	saveSettings();    	
/*    	BackgroundFill myBF = new BackgroundFill(Color.valueOf(getColorString(backgroundColour)),
				CornerRadii.EMPTY, Insets.EMPTY);
		mainPane.setBackground(new Background(myBF));*/
		
    	displayMsg.setStyle(MainController.CSS_STYLE_TEXT + getColorString(displayMsgColor));
    	displayMsg.setText(RETURN_MSG);
    	
        POMPOM.saveSettings(storageFilePath);
    }  
	
    

	public void showSingleFileChooser() {
		FileChooser fileChooser = new FileChooser(); 
		File selectedPath = fileChooser.showOpenDialog(null);
		storageLocationString.setText(selectedPath.getPath());
	}
	public void saveSettings(){
    	currentSettings.setBackgroundColour(getColorString(backgroundColour));
    	currentSettings.setReturnMsgColour(getColorString(displayMsgColor));
    	currentSettings.setInputTxtColour(getColorString(commandTextColor));
	}

}
