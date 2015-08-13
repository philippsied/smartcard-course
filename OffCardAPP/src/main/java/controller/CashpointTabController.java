package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.smartcardio.CardException;

import clientAPI.Wallet;
import controller.data.AmountKV;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CashpointTabController implements Initializable {

	private Wallet wallet;

	@FXML
	private ComboBox<AmountKV> chooseMoneyCombo;

	@FXML
	private TextField displayMoneyField;

	@FXML
	protected void handleChargeAction() {
		try {
			short currentvalue = chooseMoneyCombo.getSelectionModel().getSelectedItem().getValue();
			wallet.addMoney(currentvalue);
			displayMoneyField.setText("OK!");
		} catch (CardException e) {
			displayMoneyField.setText("ERROR!");
		}
	}

	@FXML
	protected void handleRequestAction() {
		try {
			displayMoneyField.setText(wallet.checkBalance() + "Cent");
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
