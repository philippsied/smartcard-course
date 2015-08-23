package clientAPI;

import javax.smartcardio.CardException;

/**
 * Schnittstelle zur Festlegung der Funktionen des Personaldaten-Applets.
 * 
 */
public interface PersonalData {

    /**
     * Liefert den Vornamen des Kartenbesitzers.
     * 
     * @return Vorname
     * @throws CardException
     */
    public String getFirstName() throws CardException;

    /**
     * Liefert den Nachnamen des Kartenbesitzers.
     * 
     * @return Nachname
     * @throws CardException
     */
    public String getSurname() throws CardException;

    /**
     * Liefert das Geburtsdatum des Kartenbesitzers. Die Formatierung entsprecht
     * der bei der Speicherung des Datums.
     * 
     * @return Geburtsdatum
     * @throws CardException
     */
    public String getBirthday() throws CardException;

    /**
     * Liefert den Wohnort des Kartenbesitzers.
     * 
     * @return Wohnort
     * @throws CardException
     */
    public String getLocation() throws CardException;

    /**
     * Liefert die Straße und Hausnummer des Kartenbesitzers.
     * 
     * @return Straße und Hausnummer
     * @throws CardException
     */
    public String getStreet() throws CardException;

    /**
     * Liefert die Telefonnummer des Kartenbesitzers.
     * 
     * @return Telefonnummer
     * @throws CardException
     */
    public String getPhoneNumber() throws CardException;

    /**
     * Liefert das Photo des Kartenbesitzers.
     * 
     * @return Photo
     * @throws CardException
     */
    public byte[] getPhoto() throws CardException;

    /**
     * Setzt den Vornamen des Kartenbesitzers.
     * 
     * @param newFirstName
     *            Neuer Vorname
     * @throws CardException
     */
    public void setFirstName(String newFirstName) throws CardException;

    /**
     * Setzt den Nachnamen des Kartenbesitzers.
     * 
     * @param newSurname
     *            Neuer Nachname
     * @throws CardException
     */
    public void setSurname(String newSurname) throws CardException;

    /**
     * Setzt das Geburtsdatum des Kartenbesitzers.
     * 
     * @param newBirthday
     *            Neues Geburtsdatum
     * @throws CardException
     */
    public void setBirthday(String newBirthday) throws CardException;

    /**
     * Setzt den Wohnort des Kartenbesitzers.
     * 
     * @param newLocation
     *            Neuer Wohnort
     * @throws CardException
     */
    public void setLocation(String newLocation) throws CardException;

    /**
     * Setzt die Straße und Hausnummer des Kartenbesitzers.
     * 
     * @param newStreet
     *            Neue Straße und Hausnummer
     * @throws CardException
     */
    public void setStreet(String newStreet) throws CardException;

    /**
     * Setzt die Telefonnummer des Kartenbesitzers.
     * 
     * @param newPhoneNumber
     *            Neue Telefonnummer
     * @throws CardException
     */
    public void setPhoneNumber(String newPhoneNumber) throws CardException;

    /**
     * Setzt das Photo des Kartenbesitzers.
     * 
     * @param newPhoto
     *            Neues Photo
     * @throws CardException
     */
    public void setPhoto(byte[] newPhoto) throws CardException;
}
