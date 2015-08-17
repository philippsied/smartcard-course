package clientAPI.impl;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

public abstract class GenericConnector {
    private final byte[] mAID;
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
