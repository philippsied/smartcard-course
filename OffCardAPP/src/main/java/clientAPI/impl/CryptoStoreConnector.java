package clientAPI.impl;

import java.security.interfaces.RSAPrivateKey;

import javax.smartcardio.Card;

import clientAPI.CryptoStore;

public class CryptoStoreConnector implements CryptoStore {

	private final CardConnection mConnection;

	public CryptoStoreConnector(Card card) {
		mConnection = new CardConnection(card);
	}

	public boolean authenticate(RSAPrivateKey key) {

		return false;
	}
}
