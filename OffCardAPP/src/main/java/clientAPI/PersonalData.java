package clientAPI;

import javax.smartcardio.CardException;

public interface PersonalData {

    public String getFirstName() throws CardException;

    public String getSurname() throws CardException;

    public String getBirthday() throws CardException;

    public String getLocation() throws CardException;

    public String getStreet() throws CardException;

    public String getPhoneNumber() throws CardException;

    public byte[] getPhoto() throws CardException;

    public void setFirstName(String newFirstName) throws CardException;

    public void setSurname(String newSurname) throws CardException;

    public void setBirthday(String newBirthday) throws CardException;

    public void setLocation(String newLocation) throws CardException;

    public void setStreet(String newStreet) throws CardException;

    public void setPhoneNumber(String newPhoneNumber) throws CardException;

    public void setPhoto(byte[] newPhoto) throws CardException;
}
