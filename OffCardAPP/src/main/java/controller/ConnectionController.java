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

/**
 * 
 * Controller für den Verbindungsaufbau zur Karte - Auwahl des Kartenlesegerätes
 * und Verbindungsaufbau
 *
 */
public class ConnectionController implements Initializable {

    private Consumer<Boolean> toggleFunc;
    private TitledPane parentView;

    @FXML
    private ComboBox<CardTerminal> terminalCombo;

    /**
     * Verbindungsaufbau über den "Verbinden"-Button
     * 
     * @param event
     *            Event der Mausaction
     */
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

    /**
     * Authentifizierung der Verbindung
     * 
     * @param event
     *            Event der Mausaction
     */
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

    /**
     * Abbau der Verbindung über den "Trennen"-Button
     * 
     * @param event
     *            Event der Mausaction
     */
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

    /**
     * Laden der Vorhandenen Terminals beim öffnen der ComboBox
     */
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

    /**
     * Dient dem Erhalt des Parent-Controller
     * 
     * @param parentView
     *            der Parent-Controller
     */
    public void setParentView(TitledPane parentView) {
	this.parentView = parentView;
    }

    /**
     * Setzen der Aktivität für das Aktivieren und Deaktivieren der
     * OffCard-Anwendungen je nach Verbindungszustand
     * 
     * @param func
     *            Die Funktion für das Aktivieren/Deaktivieren
     */
    public void setToggleFunction(Consumer<Boolean> func) {
	this.toggleFunc = func;
    }

    /**
     * Ausgabe der Fehler
     * 
     * @param e
     *            die Exception
     */
    private void printError(Throwable e) {
	System.err.println("ConnectionController: " + e.getLocalizedMessage());
	parentView.setText("ERROR!");
	parentView.setTextFill(Color.RED);
    }

    /**
     * Initialsierung - erstmaliges setzen der Terminals
     */
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
