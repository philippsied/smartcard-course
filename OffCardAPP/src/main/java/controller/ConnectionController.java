package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import clientAPI.ClientFactory;
import clientAPI.CryptoMgr;
import connection.LocalKeyStore;
import connection.TerminalConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;

public class ConnectionController implements Initializable {

    private Consumer<Boolean> toggleFunc;
    private TitledPane parentView;

    @FXML
    private ComboBox<CardTerminal> terminalCombo;

    @FXML
    protected void handleConnectAction(ActionEvent event) {
	try {
	    CardTerminal terminal = (CardTerminal) terminalCombo.getSelectionModel().getSelectedItem();
	    TerminalConnection.INSTANCE.chooseTerminal(terminal);
	    if (!TerminalConnection.INSTANCE.isConnected() && TerminalConnection.INSTANCE.connect()) {
		parentView.setText("Verbunden!");
		parentView.setTextFill(Color.GREEN);
		toggleFunc.accept(true);
	    } else {
		parentView.setText("ERROR!");
		parentView.setTextFill(Color.RED);
	    }
	} catch (CardException e) {
	    printError(e);
	}
    }

    @FXML
    protected void handleAuthenticateAction(ActionEvent event) {
	try {
	    if (TerminalConnection.INSTANCE.isConnected()) {
		CryptoMgr crypto = ClientFactory.getCryptoMgr(TerminalConnection.INSTANCE.getCurrentCard());
		crypto.authenticateConnection(LocalKeyStore.INSTANCE.getEncryptionFunc());

		parentView.setText("Verbunden! (Gesichert)");
		parentView.setTextFill(Color.GREEN);
		toggleFunc.accept(true);
	    } else {
		parentView.setText("ERROR!");
		parentView.setTextFill(Color.RED);
	    }
	} catch (CardException e) {
	    printError(e);
	}
    }

    @FXML
    protected void handleDisconnectAction(ActionEvent event) {
	try {
	    if (TerminalConnection.INSTANCE.isConnected() && TerminalConnection.INSTANCE.disconnect()) {
		parentView.setText("Nicht verbunden!");
		parentView.setTextFill(Color.RED);
		toggleFunc.accept(false);
	    }
	} catch (CardException e) {
	    printError(e);
	}
    }

    @FXML
    protected void handleComboShowing() {
	try {
	    int bkpIndex = terminalCombo.getSelectionModel().getSelectedIndex();
	    terminalCombo.getItems().clear();
	    terminalCombo.getItems().addAll(TerminalConnection.getTerminals());
	    terminalCombo.getSelectionModel().select(bkpIndex);
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

    public void setToggleFunction(Consumer<Boolean> func) {
	this.toggleFunc = func;
    }

    private void printError(Throwable e) {
	System.err.println("ConnectionController: " + e.getLocalizedMessage());
	parentView.setText("ERROR!");
	parentView.setTextFill(Color.RED);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	try {
	    terminalCombo.getItems().clear();
	    terminalCombo.getItems().addAll(TerminalConnection.getTerminals());
	    terminalCombo.getSelectionModel().select(0);
	} catch (CardException e) {
	    printError(e);
	}
    }

}
