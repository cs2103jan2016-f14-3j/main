package gui;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import main.POMPOM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utils.Item;


public class Controller implements Initializable{

    @FXML
    Button newTask;
    @FXML
    Button editTask;
    @FXML
    Button saveTask;
    @FXML
    Button deleteTask;
    @FXML
    Button enterCommand;
    @FXML
    TableView<Item> table;
    @FXML
    TableColumn<Item, String> taskID;
    @FXML
    TableColumn<Item, String> taskName;
    @FXML
    TableColumn<Item, Date> taskDateTime;
    @FXML
    TableColumn<Item, String> taskLabel;
    @FXML
    TableColumn<Item, String> taskPriority;
    @FXML
    TableColumn<Item, String> taskStatus;
    @FXML
    ListView<String> list;
    @FXML
    TextField inputCommand;
    
    
    
    
    ObservableList<String> taskView = FXCollections.observableArrayList();
    ObservableList<Item> tableContent = FXCollections.observableArrayList(new Item(0,"Jacob", null, "wfewfw", "wfwefwfW","fwfwfe",null,null));
    POMPOM pompom = new POMPOM();
    


	@Override
	public void initialize(URL location, ResourceBundle resources) {
        assert newTask != null : "fx:id=\"newTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert editTask != null : "fx:id=\"editTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert saveTask != null : "fx:id=\"saveTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert deleteTask != null : "fx:id=\"deleteTask\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert enterCommand != null : "fx:id=\"enterCommand\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert taskID != null : "fx:id=\"taskID\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert taskName != null : "fx:id=\"taskName\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert taskDateTime != null : "fx:id=\"taskDateTime\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert taskLabel != null : "fx:id=\"taskLabel\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert taskPriority != null : "fx:id=\"taskPriority\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert taskStatus != null : "fx:id=\"taskStatus\" was not injected: check your FXML file 'POMPOM.fxml'.";
        assert inputCommand != null : "fx:id=\"inputCommand\" was not injected: check your FXML file 'POMPOM.fxml'.";
        System.out.println(this.getClass().getSimpleName() + ".initialize");
        
        tableContent = FXCollections.observableArrayList(pompom.getStorage().getTaskList());
        configureButtons();
        configureTable();
        
	}
	
	private void configureButtons() {
        if (newTask != null) {
            newTask.setDisable(false);
        }
        if (saveTask != null) {
            saveTask.setDisable(true);
        }
        if (editTask != null) {
            editTask.setDisable(true);
        }
        if (deleteTask != null) {
            deleteTask.setDisable(true);
        }
        if (enterCommand != null) {
            enterCommand.setDisable(false);
        }
    }
	
    private void configureTable() {
    	taskName.setCellValueFactory(new PropertyValueFactory<Item, String>("title"));
        taskDateTime.setCellValueFactory(new PropertyValueFactory<Item, Date>("startDate"));
        taskLabel.setCellValueFactory(new PropertyValueFactory<Item, String>("label"));
        taskPriority.setCellValueFactory(new PropertyValueFactory<Item, String>("priority"));
        taskStatus.setCellValueFactory(new PropertyValueFactory<Item, String>("status"));
        
        table.setItems(tableContent);        
    }
    
    public void enterCommandFired(ActionEvent event) {
        String input = inputCommand.getText();
        inputCommand.clear();
        pompom.execute(input);
        inputCommand.setPromptText("Command:");
    }  
    
    public void enterCommandKey(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            String input = inputCommand.getText();
            pompom.execute(input);
            inputCommand.clear();
            inputCommand.setPromptText("Command:");
    }
        
    }  
    
    public void newTaskFired(ActionEvent event) {
        
    }    
    }
