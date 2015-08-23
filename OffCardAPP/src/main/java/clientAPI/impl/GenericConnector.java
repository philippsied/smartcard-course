package clientAPI.impl;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

/**
 * Abstrakte Klasse zur Bereitstellung des generellen Kommunikationsablaufes mit
 * der Smartcard.
 * <p>
 * Spezialisierungen dieser Klasse müssen {@code checkForError()} implementieren
 * und können über {@code genericCommand()} Befehle an das Applet senden.
 *
 */
public abstract class GenericConnector {

    /**
     * AID des Applets, mit dem kommuniziert werden soll.
     */
    private final byte[] mAID;

    /**
     * Bestehende Verbindung zur Karte
     */
    protected final CardConnection mConnection;

    public GenericConnector(byte[] AID, Card card) {
	mAID = AID;
	mConnection = new CardConnection(card);
    }

    protected ResponseAPDU genericCommand(CommandHeader command, byte[] data, short expectedResponseLength)
	    throws CardException {
	checkForError(mConnection.select(mAID));
	ResponseAPDU response = mConnection.sendAPDU(command, data, expectedResponseLength);
	checkForError(response);

	return response;
    }

    protected ResponseAPDU genericCommand(CommandHeader command, byte[] data) throws CardException {
	checkForError(mConnection.select(mAID));
	ResponseAPDU response = mConnection.sendAPDU(command, data);
	checkForError(response);

	return response;
    }

    protected abstract void checkForError(ResponseAPDU response) throws CardException;

}
