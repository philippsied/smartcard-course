package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.smartcardio.CardException;

import clientAPI.ClientFactory;
import clientAPI.Wallet;
import connection.TerminalConnection;
import controller.data.AmountKV;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

@SuppressWarnings("restriction")
public class CashpointController implements Initializable {

    @FXML
    private ComboBox<AmountKV> chooseMoneyCombo;

    @FXML
    private TextField displayMoneyField;

    @FXML
    protected void handleChargeAction() {
	try {
	    Wallet wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
	    short currentvalue = chooseMoneyCombo.getSelectionModel().getSelectedItem().getAmountInCent();
	    wallet.addMoney(currentvalue);
	    displayMoneyField.setText("OK!");
	} catch (CardException e) {
	    System.err.println("CashPointController: " + e.getLocalizedMessage());
	    displayMoneyField.setText("ERROR!");
	}
    }

    @FXML
    protected void handleRequestAction() {
	try {
	    Wallet wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
	    AmountKV currentMoney = new AmountKV(wallet.checkBalance());
	    displayMoneyField.setText(currentMoney.toString());
	} catch (CardException e) {
	    System.err.println("CashPointController: " + e.getLocalizedMessage());
	    displayMoneyField.setText("ERROR!");
	}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	chooseMoneyCombo.getItems().add(new AmountKV((short) 200));
	chooseMoneyCombo.getItems().add(new AmountKV((short) 500));
	chooseMoneyCombo.getItems().add(new AmountKV((short) 1000));
	chooseMoneyCombo.getItems().add(new AmountKV((short) 1500));
	chooseMoneyCombo.getItems().add(new AmountKV((short) 2000));
	chooseMoneyCombo.getItems().add(new AmountKV((short) 5000));
    }
}
