package gui;

import java.io.IOException;

import utils.Item;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;

/**
 * @@author Jorel
 *
 */
public class Main extends Application {
	
	
	@FXML
	Pane content;
	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		getClass();
		Parent root = FXMLLoader.load(getClass().getResource("POMPOM.fxml"));
		stage.setTitle("POMPOM");
		stage.setScene(new Scene(root, 800, 565));
		
		stage.show();
	}

	public boolean newTaskDialog() {
		try {
			FXMLLoader loader = new FXMLLoader(
					Main.class.getResource("NewTask.fxml"));
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
	
	public boolean editTaskDialog(Item item) {
		try {
			FXMLLoader loader = new FXMLLoader(
					Main.class.getResource("EditTask.fxml"));
			Pane page = (Pane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Task");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			EditTaskController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setItem(item);

			dialogStage.showAndWait();
			return controller.isOkClicked();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	public void helpDialog() {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("Help.fxml"));
			Pane page = (Pane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Help");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
