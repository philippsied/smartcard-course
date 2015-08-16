package clientAPI.impl;

import javax.smartcardio.Card;

import clientAPI.PersonalData;

public class PersonalDataConnector implements PersonalData {
	private final CardConnection mConnection;

	public PersonalDataConnector(Card card) {
		mConnection = new CardConnection(card);
	}
}
