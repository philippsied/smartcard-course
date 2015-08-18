package clientAPI;

import javax.smartcardio.CardException;

import clientAPI.data.Ticket;
import clientAPI.data.Trip;

public interface TicketManager {

    public void setTicket(Ticket newTicket) throws CardException;

    public Ticket getTicket() throws CardException;

    public Ticket getPreviousTicket() throws CardException;

    public void startTrip(Trip start) throws CardException;

    public void finishTrip() throws CardException;

    public Trip getTrip() throws CardException;
}
