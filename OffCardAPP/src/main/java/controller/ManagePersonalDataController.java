package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import javax.smartcardio.CardException;

import clientAPI.ClientFactory;
import clientAPI.PersonalData;
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

/**
 * 
 * Controller für Verwaltung der Personendaten - Eintragen der Daten und dem
 * Bild
 *
 */
public class ManagePersonalDataController implements Initializable {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private File file = null;
    private Image defaultPhoto = null;

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

    /**
     * Einträge aus den Feldern auf die Karte übertragen - wird ausgeführt durch
     * den "Setzen"-Button
     */
    @FXML
    protected void handleSetAction() {
	try {
	    PersonalData personal = ClientFactory.getPersonalData(TerminalConnection.INSTANCE.getCurrentCard());
	    personal.setFirstName(getFieldText(fnameField));
	    personal.setSurname(getFieldText(surnameField));
	    personal.setBirthday(getDatePickerText());
	    personal.setLocation(getFieldText(locationField));
	    personal.setStreet(getFieldText(streetField));
	    personal.setPhoneNumber(getFieldText(phonenField));
	    personal.setPhoto(getImageView());
	} catch (CardException e) {
	    System.err.println("ManagePersonalDataController: " + e.getLocalizedMessage());
	    fnameField.setText("ERROR");
	}
    }

    /**
     * Liest die Daten von der Karte ein und setzt sie in die entsprechenden
     * Felder in der View - Wird durch den "Abfrage"-Button ausgelöst
     */
    @FXML
    protected void handleGetAction() {
	try {
	    PersonalData personal = ClientFactory.getPersonalData(TerminalConnection.INSTANCE.getCurrentCard());
	    fnameField.setText(personal.getFirstName());
	    surnameField.setText(personal.getSurname());
	    setDatePickerText(personal.getBirthday());
	    locationField.setText(personal.getLocation());
	    streetField.setText(personal.getStreet());
	    phonenField.setText(personal.getPhoneNumber());
	    setImageView(personal.getPhoto());
	} catch (CardException e) {
	    System.err.println("ManagePersonalDataController: " + e.getLocalizedMessage());
	    fnameField.setText("ERROR");
	}
    }

    /**
     * Ausgewähltes Bild aus Datei lesen und als Byte-Array zurückgeben
     * 
     * @return Bild als Byte-Array
     */
    private byte[] getImageView() {
	if (file != null) {
	    return (new PictureConverter()).resizeAsByteArray(file);
	} else {
	    return "NA".getBytes();
	}
    }

    /**
     * Setzt das Bild der ImageView aus einem Byte-Array
     * 
     * @param imageAsBytes
     *            Bild als Byte-Array
     */
    private void setImageView(byte[] imageAsBytes) {
	if (imageAsBytes.length == "NA".length() && new String(imageAsBytes).equals("NA")) {
	    imageV.setImage(defaultPhoto);
	    return;
	}
	BufferedImage bi = (new PictureConverter()).writeImage(imageAsBytes);
	imageV.setImage(SwingFXUtils.toFXImage(bi, null));
    }

    /**
     * Liest ein TextField ein und gibt den Text als String zuürck
     * 
     * @param textField
     *            das TextField
     * @return Inhalt des TextFields als String
     */
    private String getFieldText(TextField textField) {
	return (textField.getText().length() != 0) ? textField.getText() : "";
    }

    /**
     * Setzt den DatePicker aus einem String
     * 
     * @param date
     *            Datum als String
     */
    private void setDatePickerText(String date) {
	try {
	    if (date != null) {
		bdayField.setValue(LocalDate.parse(date, dateTimeFormatter));
	    } else {
		bdayField.setValue(null);
	    }
	} catch (DateTimeParseException e) {
	    System.err.println("Incorrect birthday");
	}
    }

    /**
     * Liest den DatePicker ein und gibt das Datum als String zurück
     * 
     * @return Das Datum als String
     */
    private String getDatePickerText() {
	if (bdayField.getValue() != null) {
	    return dateTimeFormatter.format(bdayField.getValue());
	} else {
	    return "";
	}
    }

    /**
     * Initialisierung der UI - setzen von default-Werten
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
	surnameField.setPromptText("Mustermann");
	fnameField.setPromptText("Max");
	bdayField.setPromptText("10.02.1985");
	locationField.setPromptText("Berlin");
	streetField.setPromptText("Musterstraße 12");
	phonenField.setPromptText("030/123456");
	imageV.setOnMouseClicked((MouseEvent event) -> {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Open Resource File");
	    file = fileChooser.showOpenDialog(imageV.getScene().getWindow());

	    if (file != null) {
		PictureConverter pc = new PictureConverter();
		Image image = SwingFXUtils.toFXImage(pc.resizeAsBufferedImage(file), null);
		imageV.setImage(image);
	    }
	});
	try {
	    String url = new URL(location, "style/unknown.png").toExternalForm();
	    defaultPhoto = new Image(url);
	} catch (MalformedURLException e) {
	    System.err.println("ERROR: Set default photo");
	}
    }
}
