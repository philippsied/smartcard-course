package clientAPI.impl;

import static clientAPI.impl.OncardAPI.WalletOncard.ADD_MONEY;
import static clientAPI.impl.OncardAPI.WalletOncard.AID;
import static clientAPI.impl.OncardAPI.WalletOncard.CHECK_BALANCE;
import static clientAPI.impl.OncardAPI.WalletOncard.REM_MONEY;

import java.nio.ByteBuffer;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

import clientAPI.Wallet;

public class WalletConnector implements Wallet {

	private final CardConnection mConnection;

	public WalletConnector(Card card) {
		mConnection = new CardConnection(card);
	}

	@Override
	public void addMoney(short amountInCent) throws CardException {
		checkForError(mConnection.select(AID));
		ResponseAPDU response = mConnection.sendAPDU(ADD_MONEY, CryptoHelper.signData(amountInCent), (short) 0);
		checkForError(response);
	}

	@Override
	public void removeMoney(short amountInCent) throws CardException {
		checkForError(mConnection.select(AID));
		ResponseAPDU response = mConnection.sendAPDU(REM_MONEY, CryptoHelper.signData(amountInCent), (short) 0);
		checkForError(response);
	}

	@Override
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
