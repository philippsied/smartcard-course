package controller;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import clientAPI.impl.CardConnection;
import connection.TerminalConnection;
import controller.data.PictureConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PersonalDataController {
	
	private static final byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'P', 'e', 'r', 's', 'S', 't', 'o', 'r', 'e' };
	
	private static final byte[] GET_FNAME = { (byte) 0xE0, 0x1B, 0x00, 0x00, 0x00 };
	private static final byte[] GET_SURNAME = { (byte) 0xE0, 0x2B, 0x00, 0x00, 0x00 };
	private static final byte[] GET_BDAY = { (byte) 0xE0, 0x3B, 0x00, 0x00 };
	private static final byte[] GET_LOCATION = { (byte) 0xE0, 0x4B, 0x00, 0x00 };
	private static final byte[] GET_STREET = { (byte) 0xE0, 0x5B, 0x00, 0x00 };
	private static final byte[] GET_PHONEN = { (byte) 0xE0, 0x6B, 0x00, 0x00 };
	private static final byte[] GET_PIC = { (byte) 0xE0, 0x7B, 0x00, 0x00 };
	
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
	private ImageView imageV;
	
	@FXML
	protected void handleGetAction() {
		CommandAPDU select = new CommandAPDU(0x00, 0xA4, 0x04, 0x00, AID);
		send(select);
		fnameField.setText(setFieldFromCard(GET_FNAME));
		surnameField.setText(setFieldFromCard(GET_SURNAME));
		bdayField.setText(setFieldFromCard(GET_BDAY));
		locationField.setText(setFieldFromCard(GET_LOCATION));
		streetField.setText(setFieldFromCard(GET_STREET));
		phonenField.setText(setFieldFromCard(GET_PHONEN));
		
		imageV.setImage(setPicFromCard());
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
