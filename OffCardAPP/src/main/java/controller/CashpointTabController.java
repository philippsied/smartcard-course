package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import connection.Connection;
import controller.data.AmountKV;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CashpointTabController implements Initializable{

	private static final byte[] AID = { (byte) 0x00, 0x01, 0x02, 0x03, 0x04, 0x00 };
	private static final byte[] ADD_MONEY = { (byte) 0xE0, 0x30, 0x00, 0x00, 0x02, 0x00, 0x00 };
	//private static final byte[] SET_MONEY = { (byte) 0xE0, 0x10, 0x00, 0x00, 0x02, 0x00, 0x0A };
	private static final byte[] GET_MONEY = { (byte) 0xE0, 0x20, 0x00, 0x00, 0x02 };
	
	@FXML
	private ComboBox<AmountKV> chooseMoneyCombo;
	
	@FXML
	private TextField displayMoneyField;
	
	@FXML
	protected void handleChargeAction() {
		
		CommandAPDU select = new CommandAPDU(0x00, 0xA4, 0x04, 0x00, AID);
		Connection.send(select);
		
		byte[] setins = ADD_MONEY;
		int currentvalue = chooseMoneyCombo.getSelectionModel().getSelectedItem().getValue();
		setins[setins.length-1] = (byte) currentvalue; // Wert austauschen
		CommandAPDU setamount = new CommandAPDU(setins);
		Connection.send(setamount);
		
		displayMoneyField.setText("OK!");
	}
	
	@FXML
	protected void handleRequestAction() {
		
		CommandAPDU select = new CommandAPDU(0x00, 0xA4, 0x04, 0x00, AID);
		Connection.send(select);
		
		CommandAPDU getamount = new CommandAPDU(GET_MONEY);
		ResponseAPDU tmp = Connection.send(getamount);
		
		displayMoneyField.setText(convert(tmp.getData()) + "€");

	}
	
	public String convert(byte[] data) {

		String test = "";
		for (byte b : data)
			test += Integer.toHexString(b & 0xff);

		return String.valueOf((Integer.parseInt(test, 16)));

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		for (int i = 5; i <= 100; i+=5) {
			chooseMoneyCombo.getItems().add(new AmountKV(i));
		}

	}
	
	

}
