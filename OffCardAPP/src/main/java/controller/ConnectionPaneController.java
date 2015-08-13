package controller;

import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import connection.TerminalConnection;
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
		try {
			CardTerminal terminal = (CardTerminal) terminalCombo.getSelectionModel().getSelectedItem();
			TerminalConnection.INSTANCE.chooseTerminal(terminal);
			if (TerminalConnection.INSTANCE.connect()) {
				parentView.setText("Verbunden!");
				parentView.setTextFill(Color.GREEN);
			} else {
				parentView.setText("ERROR!");
			}
		} catch (CardException e) {
			printError(e);
		}
	}

	@FXML
	protected void handleDisconnectAction(ActionEvent event) {
		try {
			if (TerminalConnection.INSTANCE.disconnect()) {
				parentView.setText("Nicht verbunden!");
				parentView.setTextFill(Color.RED);
			}
		} catch (CardException e) {
			printError(e);
		}
	}

	@FXML
	protected void handleComboShowing() {
		try {
			List<CardTerminal> terminals = TerminalConnection.getTerminals();
			terminalCombo.getItems().clear();
			terminals.forEach(t -> terminalCombo.getItems().add(t));
		} catch (CardException e) {
			printError(e);
		}
	}

	@FXML
	protected void handleComboClicked() {
		// Nothing To do
	}

	public void setParentView(TitledPane parentView) {
		this.parentView = parentView;
	}

	private void printError(Throwable e) {
		System.err.println(e.getLocalizedMessage());
		parentView.setText("ERROR!");
		parentView.setTextFill(Color.RED);
	}
}
