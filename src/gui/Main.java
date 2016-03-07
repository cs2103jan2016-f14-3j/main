package gui;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;


public class Main extends Application {

	@FXML
	Button fuckyou;
	
	@FXML
	protected void hello(ActionEvent event){
		System.out.println("bitch");
	}
	
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("POMPOM.fxml"));
        stage.setTitle("POMPOM");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }
}
