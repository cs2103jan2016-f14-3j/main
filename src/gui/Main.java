package gui;

import java.io.IOException;

import utils.Item;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		getClass();
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource("POMPOM.fxml"));
		Parent root = FXMLLoader.load(getClass().getResource("POMPOM.fxml"));

		stage.setTitle("POMPOM");
		stage.setScene(new Scene(root, 800, 556));
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

}
