package controller;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import clientAPI.impl.CardConnection;
import connection.TerminalConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ManagePersonalDataController implements Initializable {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private static final byte[] AID = { (byte) 0x00, 0x01, 0x02, 0x03, 0x05, 0x00 };

	private static final byte[] GET_FNAME = { (byte) 0x70, 0x1B, 0x00, 0x00, 0x00 };
	private static final byte[] GET_SURNAME = { (byte) 0x70, 0x2B, 0x00, 0x00, 0x00 };
	private static final byte[] GET_BDAY = { 0x70, 0x3B, 0x00, 0x00 };
	private static final byte[] GET_LOCATION = { 0x70, 0x4B, 0x00, 0x00 };
	private static final byte[] GET_STREET = { 0x70, 0x5B, 0x00, 0x00 };
	private static final byte[] GET_PHONEN = { 0x70, 0x6B, 0x00, 0x00 };
	// private static final byte[] GET_PIC = { 0x70, 0x7B, 0x00, 0x00 };

	private static final byte[] SET_FNAME = { 0x70, 0x1A, 0x00, 0x00 };
	private static final byte[] SET_SURNAME = { 0x70, 0x2A, 0x00, 0x00 };
	private static final byte[] SET_BDAY = { 0x70, 0x3A, 0x00, 0x00 };
	private static final byte[] SET_LOCATION = { 0x70, 0x4A, 0x00, 0x00 };
	private static final byte[] SET_STREET = { 0x70, 0x5A, 0x00, 0x00 };
	private static final byte[] SET_PHONEN = { 0x70, 0x6A, 0x00, 0x00 };
	// private static final byte[] SET_PIC = { 0x70, 0x7A, 0x00, 0x00 };

	@FXML
	private TextField fnameField;

	@FXML
	private TextField surnameField;

	@FXML
	private DatePicker bdayField;

	@FXML
	private TextField locationField;

	@FXML
	private TextField streetField;

	@FXML
	private TextField phonenField;

	@FXML
	protected void handleSetAction() {

		CommandAPDU select = new CommandAPDU(0x00, 0xA4, 0x04, 0x00, AID);
		send(select);

		sendFieldToCard(SET_FNAME, getFieldText(fnameField));
		sendFieldToCard(SET_SURNAME, getFieldText(surnameField));
		sendFieldToCard(SET_BDAY, getDatePickerText(bdayField));
		sendFieldToCard(SET_LOCATION, getFieldText(locationField));
		sendFieldToCard(SET_STREET, getFieldText(streetField));
		sendFieldToCard(SET_PHONEN, getFieldText(phonenField));
	}

	@FXML
	protected void handleGetAction() {
		CommandAPDU select = new CommandAPDU(0x00, 0xA4, 0x04, 0x00, AID);
		send(select);
		fnameField.setText(setFieldFromCard(GET_FNAME));
		surnameField.setText(setFieldFromCard(GET_SURNAME));
		locationField.setText(setFieldFromCard(GET_LOCATION));
		streetField.setText(setFieldFromCard(GET_STREET));
		phonenField.setText(setFieldFromCard(GET_PHONEN));

		try {
			bdayField.setValue(LocalDate.parse(setFieldFromCard(GET_BDAY), dateTimeFormatter));
		} catch (DateTimeParseException e) {
			System.err.println("Fehlerhaftes oder nicht gesetztes Geburtsdatum");
		}
	}

	public void sendFieldToCard(byte[] dest, String toSend) {
		CommandAPDU query = new CommandAPDU(dest[0], dest[1], dest[2], dest[3], toSend.getBytes());
		send(query);
	}

	private String getFieldText(TextField textField) {
		if (textField.getText().length() != 0) {
			return textField.getText();
		} else {
			return textField.getPromptText();
		}
	}

	private String getDatePickerText(DatePicker datePicker) {
		if (bdayField.getValue() != null) {
			return dateTimeFormatter.format(bdayField.getValue());
		} else {
			return bdayField.getPromptText();
		}
	}

	public String setFieldFromCard(byte[] src) {
		CommandAPDU tmp = new CommandAPDU(src);
		ResponseAPDU answer = send(tmp);
		return new String(answer.getData(), StandardCharsets.UTF_8);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		surnameField.setPromptText("Mustermann");
		fnameField.setPromptText("Max");
		bdayField.setPromptText("10.02.1985");
		locationField.setPromptText("Berlin");
		streetField.setPromptText("Musterstraße 12");
		phonenField.setPromptText("01");
	}

	/**
	 * Temporärer Workaround
	 * 
	 * @return
	 */
	private ResponseAPDU send(CommandAPDU cmd) {
		try {
			CardConnection cc = new CardConnection(TerminalConnection.INSTANCE.getCurrentCard());
			return cc.sendAPDU(cmd);
		} catch (CardException e) {
			System.err.println("Error");
		}
		return null;
	}
}
