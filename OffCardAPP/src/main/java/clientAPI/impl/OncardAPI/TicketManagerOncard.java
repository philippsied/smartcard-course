package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

/**
 * Schnittstelle zur Low-Level Bekanntmachung der Konstanten, Instruktionen und
 * Fehlercodes des TicketManger-Applets.
 *
 */
public interface TicketManagerOncard {

    /**
     * OnCard AID
     */
    public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'T', 'i', 'c', 'k', 'e', 't', 'M', 'g', 'r' };

    
    /* -------------------- OnCard Konstanten ------------------------------------ */

    /**
     * Maximallänge der Ticketbeschreibung
     */
    public static final byte TICKET_DESCRIPTION_MAX_LENGTH = (byte) 30;
    
    /**
     * Minimallänge eines Tickets
     */
    public static final byte TICKET_MIN_SIZE = (byte) 2 * Integer.BYTES;
    
    /**
     * Maximallänge eines Tickets
     */
    public static final byte TICKET_MAX_SIZE = TICKET_MIN_SIZE + TICKET_DESCRIPTION_MAX_LENGTH;

    /**
     * Maximallänge eines Stationsnamen einer Fahrt
     */
    public static final byte TRIP_DEPARTURE_MAX_LENGTH = (byte) 30;
    
    /**
     * Minimallänge einer Fahrt
     */
    public static final byte TRIP_MIN_SIZE = (byte) Integer.BYTES;
    
    /**
     * Maximallänge einer Fahrt
     */
    public static final byte TRIP_MAX_SIZE = TRIP_MIN_SIZE + TRIP_DEPARTURE_MAX_LENGTH;

    
    /* -------------------- OnCard Anweisungen ------------------------------------ */

    /**
     * APDU für Setzen eines Tickets.
     */
    public static final CommandHeader SET_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00, (byte) 0x00, TICKET_MAX_SIZE, CmdType.LC_NoLE);

    /**
     * APDU für Abfragen eines Tickets.
     */
    public static final CommandHeader GET_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TICKET_MAX_SIZE);

    /**
     * APDU für Abfragen des Vorgängertickets.
     */
    public static final CommandHeader GET_PREV_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x30, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TICKET_MAX_SIZE);

    /**
     * APDU für Starten einer Fahrt.
     */
    public static final CommandHeader START_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xA0, (byte) 0x00, (byte) 0x00, TRIP_MAX_SIZE, CmdType.LC_NoLE);

    /**
     * APDU für Abschließen und Entfernen einer Fahrt.
     */
    public static final CommandHeader FINISH_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xB0, (byte) 0x00, (byte) 0x00, CmdType.NoLC_NoLE);

    /**
     * APDU für Abfrage einer Fahrt.
     */
    public static final CommandHeader GET_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xC0, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TRIP_MAX_SIZE);

    
    /* -------------------- OnCard Error-Codes ------------------------------------ */
    
    /**
     * Signalisiert, dass keine neue Fahrt begonnen werden kann, solange eine Fahrt noch offen ist.
     */
    public final static short ERROR_PENDING_TICKET = 0x6A30;
}
