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

public class CashpointTabController implements Initializable {

	private Wallet wallet = null;

	@FXML
	private ComboBox<AmountKV> chooseMoneyCombo;

	@FXML
	private TextField displayMoneyField;

	@FXML
	protected void handleChargeAction() {
		try {
			if (TerminalConnection.INSTANCE.connect()) {
				if (wallet == null) {
					wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
				}
				short currentvalue = chooseMoneyCombo.getSelectionModel().getSelectedItem().getValue();
				wallet.addMoney(currentvalue);
				displayMoneyField.setText("OK!");
			} else {
				System.out.println("Cashpoint: No card present");
			}
		} catch (CardException e) {
			displayMoneyField.setText("ERROR!");
		}
	}

	@FXML
	protected void handleRequestAction() {
		try {
			if (TerminalConnection.INSTANCE.connect()) {
				if (wallet == null) {
					wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
				}
				displayMoneyField.setText(wallet.checkBalance() + "Cent");
			} else {
				System.out.println("Cashpoint: No card present");
			}
		} catch (CardException e) {
			displayMoneyField.setText("ERROR!");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		for (short i = 5; i <= 100; i += 5) {
			chooseMoneyCombo.getItems().add(new AmountKV(i));
		}
	}
}
