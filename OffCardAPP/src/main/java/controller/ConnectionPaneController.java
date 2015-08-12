package controller;

import java.util.List;

import javax.smartcardio.CardTerminal;

import connection.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ConnectionPaneController {

	@FXML
	private ComboBox<CardTerminal> terminalCombo;
	@FXML
	private AnchorPane childConnection;

	private TitledPane parentView;

	@FXML
	protected void handleConnectAction(ActionEvent event) {
		if (Connection.connect()) {
			parentView.setText("Verbunden!");
			parentView.setTextFill(Color.GREEN);
		}
	}

	@FXML
	protected void handleDisconnectAction(ActionEvent event) {
		if (Connection.disconnect()) {
			parentView.setText("Nicht verbunden!");
			parentView.setTextFill(Color.RED);
		}
	}

	@FXML
	protected void handleComboShowing() {
		List<CardTerminal> terminals = Connection.getTerminals();
		terminalCombo.getItems().clear();
		terminals.forEach(t -> terminalCombo.getItems().add(t));
	}

	@FXML
	protected void handleComboClicked() {
		CardTerminal terminal = (CardTerminal) terminalCombo.getSelectionModel().getSelectedItem();
		Connection.setTerminal(terminal);
	}

	public void setParentView(TitledPane parentView) {
		this.parentView = parentView;
	}

}
