package clientAPI.impl;

import java.security.interfaces.RSAPrivateKey;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

import clientAPI.CryptoMgr;
import clientAPI.impl.OncardAPI.CryptoMgrOncard;

public class CryptoMgrConnector extends GenericConnector implements CryptoMgr {

    public CryptoMgrConnector(Card card) {
	super(CryptoMgrOncard.AID, card);
    }

    public boolean authenticate(RSAPrivateKey key) {

	return false;
    }

    @Override
    protected void checkForError(ResponseAPDU response) throws CardException {
	if (response.getSW() != 0x9000)
	    throw new CardException("Error: " + Integer.toHexString(response.getSW() & 0xffff));
    }
}
