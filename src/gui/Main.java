package gui;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	getClass();
    	Parent root = FXMLLoader.load(getClass().getResource("POMPOM.fxml"));
        stage.setTitle("POMPOM");
        stage.setScene(new Scene(root, 800, 556));
        stage.show();
        
    }
    
}
