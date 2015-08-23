package clientAPI;

import javax.smartcardio.CardException;

import clientAPI.data.Ticket;
import clientAPI.data.Trip;

/**
 * Schnittstelle zur Festlegung der Funktionalität des Ticket- und
 * Fahrten-Applets.
 *
 *
 */
public interface TicketManager {

    /**
     * Speichert das angegebene Ticket auf der Smartcard. Das alte Ticket wird
     * dabei gespeichert.
     * 
     * @param newTicket
     *            neues Ticket
     * @throws CardException
     */
    public void setTicket(Ticket newTicket) throws CardException;

    /**
     * Liefert das aktuell auf der Karte gespeicherte Ticket.
     * 
     * @return Aktuelles Ticket oder {@code null}, falls keines gespeichert ist.
     * @throws CardException
     */
    public Ticket getTicket() throws CardException;

    /**
     * Liefert das zuvor verwendete und gespeicherte Ticket.
     * 
     * @return Abgelaufenes Ticket
     * @throws CardException
     */
    public Ticket getPreviousTicket() throws CardException;

    /**
     * Startet eine Fahrt und speichert diese auf der Karte.
     * 
     * @param start
     * @throws CardException
     */
    public void startTrip(Trip start) throws CardException;

    /**
     * Beendet die Fahrt, wodurch die Fahrt aus dem Speicher der Karte gelöscht
     * wird.
     * 
     * @throws CardException
     */
    public void finishTrip() throws CardException;

    /**
     * Liefert die aktuell gesetzte Fahrt.
     * 
     * @return Aktuelle Fahrt oder {@code null}, falls keine gespeichert ist.
     * @throws CardException
     */
    public Trip getTrip() throws CardException;
}
