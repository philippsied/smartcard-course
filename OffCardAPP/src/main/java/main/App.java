package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("UI/offcard.fxml"));

		Scene scene = new Scene(root, 700, 400);

		stage.setTitle("SmartCard-Projekt");
		stage.setScene(scene);
		stage.show();
	}

}
