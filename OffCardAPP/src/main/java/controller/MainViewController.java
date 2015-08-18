package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;

public class MainViewController implements Initializable {

    @FXML
    private TitledPane accessControlStartPane;
    @FXML
    private TitledPane accessControlEndPane;
    @FXML
    private TitledPane cashpointPane;
    @FXML
    private TitledPane ticketPane;
    @FXML
    private TitledPane personalPane;
    @FXML
    private TitledPane administrationPane;
    @FXML
    private TitledPane connectionPane;
    @FXML
    private ConnectionController cpController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	cpController.setParentView(connectionPane);
	connectionPane.setCollapsible(false);
	cpController.setToggleFunction(b -> setTerminalState(b));
	setTerminalState(false);
    }

    public void setTerminalState(boolean isEnabled) {
	accessControlStartPane.setDisable(!isEnabled);
	accessControlEndPane.setDisable(!isEnabled);
	cashpointPane.setDisable(!isEnabled);
	ticketPane.setDisable(!isEnabled);
	personalPane.setDisable(!isEnabled);
	administrationPane.setDisable(!isEnabled);
    }

}
