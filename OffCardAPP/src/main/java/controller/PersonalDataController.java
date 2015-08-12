package controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import connection.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class PersonalDataController {
	
	private static final byte[] AID = { (byte) 0x00, 0x01, 0x02, 0x03, 0x05, 0x00 };
	
	private static final byte[] GET_FNAME = { (byte) 0x70, 0x1B, 0x00, 0x00, 0x00 };
	private static final byte[] GET_SURNAME = { (byte) 0x70, 0x2B, 0x00, 0x00, 0x00 };
	private static final byte[] GET_BDAY = { 0x70, 0x3B, 0x00, 0x00 };
	private static final byte[] GET_LOCATION = { 0x70, 0x4B, 0x00, 0x00 };
	private static final byte[] GET_STREET = { 0x70, 0x5B, 0x00, 0x00 };
	private static final byte[] GET_PHONEN = { 0x70, 0x6B, 0x00, 0x00 };
	private static final byte[] GET_PIC = { 0x70, 0x7B, 0x00, 0x00 };
	
	@FXML
	private TextField fnameField;

	@FXML
	private TextField surnameField;

	@FXML
	private TextField bdayField;

	@FXML
	private TextField locationField;

	@FXML
	private TextField streetField;

	@FXML
	private TextField phonenField;
	
	
	@FXML
	protected void handleGetAction() {
		CommandAPDU select = new CommandAPDU(0x00, 0xA4, 0x04, 0x00, AID);
		Connection.send(select);
		fnameField.setText(setFieldFromCard(GET_FNAME));
		surnameField.setText(setFieldFromCard(GET_SURNAME));
		bdayField.setText(setFieldFromCard(GET_BDAY));
		locationField.setText(setFieldFromCard(GET_LOCATION));
		streetField.setText(setFieldFromCard(GET_STREET));
		phonenField.setText(setFieldFromCard(GET_PHONEN));
	}
	
	public String setFieldFromCard(byte[] src) {
		CommandAPDU tmp = new CommandAPDU(src);
		ResponseAPDU answer = Connection.send(tmp);
		return new String(answer.getData(), StandardCharsets.UTF_8);
	}
	
	

}
