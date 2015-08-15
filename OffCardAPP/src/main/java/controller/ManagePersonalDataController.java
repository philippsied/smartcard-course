package controller;

import java.awt.image.BufferedImage;
import java.io.File;
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
import controller.data.PictureConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class ManagePersonalDataController implements Initializable {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private File file = null;
	
	private static final byte[] AID = { (byte) 0x00, 0x01, 0x02, 0x03, 0x05, 0x00 };

	private static final byte[] GET_FNAME = { (byte) 0x70, 0x1B, 0x00, 0x00, 0x00 };
	private static final byte[] GET_SURNAME = { (byte) 0x70, 0x2B, 0x00, 0x00, 0x00 };
	private static final byte[] GET_BDAY = { (byte) 0xE0, 0x3B, 0x00, 0x00 };
	private static final byte[] GET_LOCATION = { (byte) 0xE0, 0x4B, 0x00, 0x00 };
	private static final byte[] GET_STREET = { (byte) 0xE0, 0x5B, 0x00, 0x00 };
	private static final byte[] GET_PHONEN = { (byte) 0xE0, 0x6B, 0x00, 0x00 };
	private static final byte[] GET_PIC = { (byte) 0xE0, 0x7B, 0x00, 0x00 };

	private static final byte[] SET_FNAME = { (byte) 0xE0, 0x1A, 0x00, 0x00 };
	private static final byte[] SET_SURNAME = { (byte) 0xE0, 0x2A, 0x00, 0x00 };
	private static final byte[] SET_BDAY = { (byte) 0xE0, 0x3A, 0x00, 0x00 };
	private static final byte[] SET_LOCATION = { (byte) 0xE0, 0x4A, 0x00, 0x00 };
	private static final byte[] SET_STREET = { (byte) 0xE0, 0x5A, 0x00, 0x00 };
	private static final byte[] SET_PHONEN = { (byte) 0xE0, 0x6A, 0x00, 0x00 };
	private static final byte[] SET_PIC = { (byte) 0xE0, 0x7A, 0x00, 0x00 };

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
	private ImageView imageV;

	@FXML
	protected void handleSetAction() {

		CommandAPDU select = new CommandAPDU(0x00, 0xA4, 0x04, 0x00, AID);
		send(select);

		sendFieldToCard(SET_FNAME, getFieldText(fnameField).getBytes());
		sendFieldToCard(SET_SURNAME, getFieldText(surnameField).getBytes());
		sendFieldToCard(SET_BDAY, getDatePickerText(bdayField).getBytes());
		sendFieldToCard(SET_LOCATION, getFieldText(locationField).getBytes());
		sendFieldToCard(SET_STREET, getFieldText(streetField).getBytes());
		sendFieldToCard(SET_PHONEN, getFieldText(phonenField).getBytes());
		sendPicToCard(SET_PIC);
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
		
		imageV.setImage(setPicFromCard());
	
		try {
			bdayField.setValue(LocalDate.parse(setFieldFromCard(GET_BDAY), dateTimeFormatter));
		} catch (DateTimeParseException e) {
			System.err.println("Incorrect or unset birth");
		}
	}

	private void sendPicToCard(byte[] setPic) {
		
		if(file != null){
			PictureConverter pc = new PictureConverter();
			byte[] pic = pc.resizeAsByteArray(file);
			sendFieldToCard(setPic, pic);
		}
		
	}

	public void sendFieldToCard(byte[] dest, byte[] toSend) {
		CommandAPDU query = new CommandAPDU(dest[0], dest[1], dest[2], dest[3], toSend);
		send(query);
	}

	public String setFieldFromCard(byte[] src) {
		CommandAPDU tmp = new CommandAPDU(src);
		ResponseAPDU answer = send(tmp);
		return new String(answer.getData(), StandardCharsets.UTF_8);
	}
	
	private Image setPicFromCard() {
		CommandAPDU query = new CommandAPDU(GET_PIC);
		ResponseAPDU answer = send(query);
		
		PictureConverter pc = new PictureConverter();
		BufferedImage bi = pc.writeImage(answer.getData());
		
		return SwingFXUtils.toFXImage(bi, null);
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		surnameField.setPromptText("Mustermann");
		fnameField.setPromptText("Max");
		bdayField.setPromptText("10.02.1985");
		locationField.setPromptText("Berlin");
		streetField.setPromptText("Musterstraße 12");
		phonenField.setPromptText("0123456");
		
		imageV.setOnMouseClicked((MouseEvent event) -> {
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			file = fileChooser.showOpenDialog(imageV.getScene().getWindow());
			
			if(file != null){
				PictureConverter pc = new PictureConverter();
				Image image = SwingFXUtils.toFXImage(pc.resizeAsBufferedImage(file), null);
				imageV.setImage(image);
			}
        });
 
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
