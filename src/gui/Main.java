package gui;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.Node;


public class Main extends Application {
	
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	getClass();
    	Group root = new Group();
    	Node nodeMenu = FXMLLoader.load(getClass().getResource("POMPOM.fxml"));
    	Scene scene = new Scene(root, 800, 556);
    	root.getChildren().add(nodeMenu);
        stage.setTitle("POMPOM");
        stage.setScene(scene);
        stage.show();
    }
        
    
	public boolean newTaskDialog() {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("NewTask.fxml"));
			Pane page = (Pane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Task");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			NewTaskController newTaskController = loader.getController();
			newTaskController.setDialogStage(dialogStage);
			
			dialogStage.showAndWait();
			
			return newTaskController.isOkClicked();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

    
}
