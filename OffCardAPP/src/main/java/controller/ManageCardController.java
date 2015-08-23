package controller;

import javax.smartcardio.CardException;

import clientAPI.ClientFactory;
import clientAPI.CryptoMgr;
import connection.LocalKeyStore;
import connection.TerminalConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller für die Einrichtung der Karte.
 *
 */
public class ManageCardController{

    @FXML
    private TextField cideField;
    @FXML
    private TextField pubkField;
    @FXML
    private Label state;

    @FXML
    protected void handleSetAction() {
	try {
	    CryptoMgr crypto = ClientFactory.getCryptoMgr(TerminalConnection.INSTANCE.getCurrentCard());
	    crypto.initializeCard(Integer.parseInt(cideField.getText()), LocalKeyStore.INSTANCE.getPublicKey());
	    pubkField.setText(crypto.getStoredPublicKey().getModulus().toString().substring(0, 11));
	    state.setText("OK");
	} catch (CardException e) {
	    System.err.println("ManageCardController: " + e.getLocalizedMessage());
	    state.setText("ERROR");
	}
    }

    @FXML
    protected void handleGetAction() {
	try {
	    CryptoMgr crypto = ClientFactory.getCryptoMgr(TerminalConnection.INSTANCE.getCurrentCard());
	    cideField.setText(Integer.toString(crypto.getCardId()));
	    pubkField.setText(crypto.getStoredPublicKey().getModulus().toString().substring(0, 11));
	    state.setText("");
	} catch (CardException e) {
	    System.err.println("ManageCardController: " + e.getLocalizedMessage());
	    cideField.setText("ERROR");
	}
    }
}
