package clientAPI.impl;

import javax.smartcardio.Card;

import clientAPI.CustomerData;

public class CustomerDataConnector implements CustomerData {
	private final CardConnection mConnection;

	public CustomerDataConnector(Card card) {
		mConnection = new CardConnection(card);
	}
}
