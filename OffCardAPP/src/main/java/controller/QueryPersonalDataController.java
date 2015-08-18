package controller;

import java.awt.image.BufferedImage;

import javax.smartcardio.CardException;

import clientAPI.ClientFactory;
import clientAPI.PersonalData;
import connection.TerminalConnection;
import controller.data.PictureConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@SuppressWarnings("restriction")
public class QueryPersonalDataController {

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
	try {
	    PersonalData personal = ClientFactory.getPersonalData(TerminalConnection.INSTANCE.getCurrentCard());
	    fnameField.setText(personal.getFirstName());
	    surnameField.setText(personal.getSurname());
	    bdayField.setText(personal.getBirthday());
	    locationField.setText(personal.getLocation());
	    streetField.setText(personal.getStreet());
	    phonenField.setText(personal.getPhoneNumber());
	    imageV.setImage(convertToImage(personal.getPhoto()));
	} catch (CardException e) {
	    System.err.println("QueryPersonalDataController: " + e.getLocalizedMessage());
	    fnameField.setText("ERROR");
	}
    }

    private Image convertToImage(byte[] photo) {
	PictureConverter pc = new PictureConverter();
	BufferedImage bi = pc.writeImage(photo);
	return SwingFXUtils.toFXImage(bi, null);
    }
}