package clientAPI.impl;

import javax.smartcardio.Card;

import clientAPI.TicketManager;

public class TicketManagerConnector implements TicketManager {

	private final CardConnection mConnection;

	public TicketManagerConnector(Card card) {
		mConnection = new CardConnection(card);
	}
}
