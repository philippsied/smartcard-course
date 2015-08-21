package clientAPI;

import java.security.interfaces.RSAPublicKey;

import javax.smartcardio.CardException;

import clientAPI.data.EncryptFunction;

public interface CryptoMgr {

    public boolean authenticateConnection(EncryptFunction encrypt) throws CardException;

    public void initializeCard(int cardId, RSAPublicKey offcardPubKey) throws CardException;

    public int getCardId() throws CardException;

    public RSAPublicKey getStoredPublicKey() throws CardException;

}
