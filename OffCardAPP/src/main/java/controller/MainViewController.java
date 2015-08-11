package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;

public class MainViewController implements Initializable {

	@FXML
	private TitledPane connectionPane;

	@FXML
	private ConnectionPaneController cpController;

	@FXML
	private TabPane userTabPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cpController.setParentView(connectionPane);
	}

}
