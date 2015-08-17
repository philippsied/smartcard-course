package clientAPI.impl;

import java.nio.ByteBuffer;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

import clientAPI.Wallet;
import clientAPI.impl.OncardAPI.WalletOncard;

public class WalletConnector extends GenericConnector implements Wallet {

    public WalletConnector(Card card) {
	super(WalletOncard.AID, card);
    }

    @Override
    protected void checkForError(ResponseAPDU response) throws CardException {
	if (response.getSW() != 0x9000)
	    throw new CardException("Error: " + Integer.toHexString(response.getSW() & 0xffff));
    }

    @Override
    public void addMoney(short amountInCent) throws CardException {
	genericCommand(WalletOncard.ADD_MONEY, CryptoHelper.signData(amountInCent), (short) 0);
    }

    @Override
    public void removeMoney(short amountInCent) throws CardException {
	genericCommand(WalletOncard.SUB_MONEY, CryptoHelper.signData(amountInCent), (short) 0);
    }

    @Override
    public short checkBalance() throws CardException {
	ResponseAPDU response = genericCommand(WalletOncard.CHECK_BALANCE, null, (short) 2);
	return ByteBuffer.wrap(response.getData()).getShort();
    }

}
