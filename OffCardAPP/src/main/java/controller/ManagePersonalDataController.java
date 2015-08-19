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

    private byte[] getImageView() {
	if (file != null) {
	    return (new PictureConverter()).resizeAsByteArray(file);
	} else {
	    return "NA".getBytes();
	}
    }

    private void setImageView(byte[] imageAsBytes) {
	if(imageAsBytes.length == "NA".length() && new String(imageAsBytes).equals("NA")){
	    return;
	}
	BufferedImage bi = (new PictureConverter()).writeImage(imageAsBytes);
	imageV.setImage(SwingFXUtils.toFXImage(bi, null));
    }

    private String getFieldText(TextField textField) {
	return (textField.getText().length() != 0) ? textField.getText() : "";
    }

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

    private String getDatePickerText() {
	if (bdayField.getValue() != null) {
	    return dateTimeFormatter.format(bdayField.getValue());
	} else {
	    return "";
	}
    }

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
	try{
	    String url = new URL(location, "style/unknown.png").toExternalForm();
	defaultPhoto = new Image(url);
	}catch(MalformedURLException e){
	   System.err.println("ERROR: Set default photo");
	}
    }
}
