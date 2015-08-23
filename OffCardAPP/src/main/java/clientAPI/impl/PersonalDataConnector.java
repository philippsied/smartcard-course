package clientAPI.impl;

import java.nio.charset.StandardCharsets;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

import clientAPI.PersonalData;
import clientAPI.impl.OncardAPI.PersonalDataOncard;

/**
 * Implementierung von {@code clientAPI.PersonalData}
 *
 */
public class PersonalDataConnector extends GenericConnector implements PersonalData {

    public PersonalDataConnector(Card card) {
	super(PersonalDataOncard.AID, card);
    }

    @Override
    protected void checkForError(ResponseAPDU response) throws CardException {
	if (response.getSW() != 0x9000)
	    throw new CardException("Error: " + Integer.toHexString(response.getSW() & 0xffff));
    }

    @Override
    public String getFirstName() throws CardException {
	ResponseAPDU response = genericCommand(PersonalDataOncard.GET_FNAME, null);
	return new String(response.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public String getSurname() throws CardException {
	ResponseAPDU response = genericCommand(PersonalDataOncard.GET_SURNAME, null);
	return new String(response.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public String getBirthday() throws CardException {
	ResponseAPDU response = genericCommand(PersonalDataOncard.GET_BDAY, null);
	return new String(response.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public String getLocation() throws CardException {
	ResponseAPDU response = genericCommand(PersonalDataOncard.GET_LOCATION, null);
	return new String(response.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public String getStreet() throws CardException {
	ResponseAPDU response = genericCommand(PersonalDataOncard.GET_STREET, null);
	return new String(response.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public String getPhoneNumber() throws CardException {
	ResponseAPDU response = genericCommand(PersonalDataOncard.GET_PHONENR, null);
	return new String(response.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public byte[] getPhoto() throws CardException {
	ResponseAPDU response = genericCommand(PersonalDataOncard.GET_PHOTO, null);
	return response.getData();
    }

    @Override
    public void setFirstName(String newFirstName) throws CardException {
	genericCommand(PersonalDataOncard.SET_FNAME, newFirstName.getBytes());
    }

    @Override
    public void setSurname(String newSurname) throws CardException {
	genericCommand(PersonalDataOncard.SET_SURNAME, newSurname.getBytes());
    }

    @Override
    public void setBirthday(String newBirthday) throws CardException {
	genericCommand(PersonalDataOncard.SET_BDAY, newBirthday.getBytes());
    }

    @Override
    public void setLocation(String newLocation) throws CardException {
	genericCommand(PersonalDataOncard.SET_LOCATION, newLocation.getBytes());
    }

    @Override
    public void setStreet(String newStreet) throws CardException {
	genericCommand(PersonalDataOncard.SET_STREET, newStreet.getBytes());
    }

    @Override
    public void setPhoneNumber(String newPhoneNumber) throws CardException {
	genericCommand(PersonalDataOncard.SET_PHONENR, newPhoneNumber.getBytes());
    }

    @Override
    public void setPhoto(byte[] newPhoto) throws CardException {
	genericCommand(PersonalDataOncard.SET_PHOTO, newPhoto);
    }
}
