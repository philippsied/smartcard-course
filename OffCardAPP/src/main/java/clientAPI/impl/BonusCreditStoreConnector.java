package clientAPI.impl;

import java.nio.ByteBuffer;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

import clientAPI.BonusCreditStore;
import clientAPI.impl.OncardAPI.BonusCreditStoreOncard;

/**
 * Implementierung von {@code clientAPI.BonusCreditStore}
 *
 */
public final class BonusCreditStoreConnector extends GenericConnector implements BonusCreditStore {

    public BonusCreditStoreConnector(Card card) {
	super(BonusCreditStoreOncard.AID, card);
    }

    @Override
    protected void checkForError(ResponseAPDU response) throws CardException {
	if (response.getSW() != 0x9000)
	    throw new CardException("Error: " + Integer.toHexString(response.getSW() & 0xffff));
    }

    public void addBonusCredits(int amount) throws CardException {
	byte[] data = ByteBuffer.allocate(Short.BYTES).putShort((short) (0x0000FFFF & amount)).array();
	genericCommand(BonusCreditStoreOncard.ADD_CREDITS, data, (short) 0);
    }

    public void removeBonusCredits(int amount) throws CardException {
	byte[] data = ByteBuffer.allocate(Short.BYTES).putShort((short) (0x0000FFFF & amount)).array();
	genericCommand(BonusCreditStoreOncard.SUB_CREDITS, data, (short) 0);
    }

    public int checkBalance() throws CardException {
	ResponseAPDU response = genericCommand(BonusCreditStoreOncard.CHECK_BALANCE, null, (short) 2);
	return ByteBuffer.wrap(response.getData()).getShort();
    }
}
