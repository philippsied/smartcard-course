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
    public void addMoney(int amountInCent) throws CardException {
	byte[] data = ByteBuffer.allocate(Short.BYTES).putShort((short) (0x0000FFFF & amountInCent)).array();
	genericCommand(WalletOncard.ADD_MONEY, data, (short) 0);
    }

    @Override
    public void removeMoney(int amountInCent) throws CardException {
	byte[] data = ByteBuffer.allocate(Short.BYTES).putShort((short) (0x0000FFFF & amountInCent)).array();
	genericCommand(WalletOncard.SUB_MONEY, data, (short) 0);
    }

    @Override
    public int checkBalance() throws CardException {
	ResponseAPDU response = genericCommand(WalletOncard.CHECK_BALANCE, null, (short) 2);
	return ByteBuffer.wrap(response.getData()).getShort();
    }

}
