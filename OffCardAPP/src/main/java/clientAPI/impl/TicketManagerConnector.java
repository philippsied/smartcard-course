package clientAPI.impl;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

import clientAPI.TicketManager;
import clientAPI.data.Ticket;
import clientAPI.data.Trip;
import clientAPI.impl.OncardAPI.TicketManagerOncard;

public class TicketManagerConnector extends GenericConnector implements TicketManager {

    public TicketManagerConnector(Card card) {
	super(TicketManagerOncard.AID, card);
    }

    @Override
    protected void checkForError(ResponseAPDU response) throws CardException {
	if (response.getSW() != 0x9000)
	    throw new CardException("Error: " + Integer.toHexString(response.getSW() & 0xffff));
    }

    @Override
    public void setTicket(Ticket newTicket) throws CardException {
	genericCommand(TicketManagerOncard.SET_TICKET, newTicket.toByteArray(), (short) 0);
    }

    @Override
    public Ticket getTicket() throws CardException {
	ResponseAPDU response = genericCommand(TicketManagerOncard.GET_TICKET, null, TicketManagerOncard.TICKET_MAX_SIZE);
	return Ticket.extractTicket(response.getData());
    }

    @Override
    public Ticket getPreviousTicket() throws CardException {
	ResponseAPDU response = genericCommand(TicketManagerOncard.GET_PREV_TICKET, null,
		TicketManagerOncard.TICKET_MAX_SIZE);
	return Ticket.extractTicket(response.getData());
    }

    @Override
    public void startTrip(Trip start) throws CardException {
	genericCommand(TicketManagerOncard.START_TRIP, start.toByteArray(), (short) 0);
    }

    @Override
    public void finishTrip() throws CardException {
	genericCommand(TicketManagerOncard.FINISH_TRIP, null, (short) 0);
    }

    @Override
    public Trip getTrip() throws CardException {
	ResponseAPDU response = genericCommand(TicketManagerOncard.GET_TRIP, null, TicketManagerOncard.TRIP_MAX_SIZE);
	return Trip.extractTrip(response.getData());
    }
}
