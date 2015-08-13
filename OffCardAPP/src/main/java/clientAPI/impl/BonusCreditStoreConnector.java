package clientAPI.impl;

import static clientAPI.impl.OncardAPI.BonusCreditStoreOncard.ADD_CREDITS;
import static clientAPI.impl.OncardAPI.BonusCreditStoreOncard.AID;
import static clientAPI.impl.OncardAPI.BonusCreditStoreOncard.CHECK_BALANCE;
import static clientAPI.impl.OncardAPI.BonusCreditStoreOncard.REM_CREDITS;

import java.nio.ByteBuffer;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

import clientAPI.BonusCreditStore;

public class BonusCreditStoreConnector implements BonusCreditStore {
	private final CardConnection mConnection;

	public BonusCreditStoreConnector(Card card) {
		mConnection = new CardConnection(card);
	}

	public void addBonusCredits(short amount) throws CardException {
		checkForError(mConnection.select(AID));
		ResponseAPDU response = mConnection.sendAPDU(ADD_CREDITS, CryptoHelper.signData(amount), (short) 0);
		checkForError(response);
	}

	public void removeBonusCredits(short amount) throws CardException {
		checkForError(mConnection.select(AID));
		ResponseAPDU response = mConnection.sendAPDU(REM_CREDITS, CryptoHelper.signData(amount), (short) 0);
		checkForError(response);
	}

	public short checkBalance() throws CardException {
		checkForError(mConnection.select(AID));
		ResponseAPDU response = mConnection.sendAPDU(CHECK_BALANCE, null, (short) 2);
		checkForError(response);
		return ByteBuffer.wrap(response.getData()).getShort();
	}

	private void checkForError(ResponseAPDU response) throws CardException {
		if (response.getSW() != 0x9000)
			throw new CardException("Error");
	}
}
