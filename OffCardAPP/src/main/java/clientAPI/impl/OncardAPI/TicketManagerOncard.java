package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

public interface TicketManagerOncard {

    /**
     * Oncard AID
     */
    public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'T', 'i', 'c', 'k', 'e', 't', 'M', 'g', 'r' };

    /*
     * Oncard Konstanten
     */

    public static final byte TICKET_DESCRIPTION_MAX_LENGTH = (byte) 30;
    public static final byte TICKET_MIN_SIZE = (byte) 2 * Integer.BYTES;
    public static final byte TICKET_MAX_SIZE = TICKET_MIN_SIZE + TICKET_DESCRIPTION_MAX_LENGTH;

    public static final byte TRIP_DEPARTURE_MAX_LENGTH = (byte) 30;
    public static final byte TRIP_MIN_SIZE = (byte) Integer.BYTES;
    public static final byte TRIP_MAX_SIZE = TRIP_MIN_SIZE + TRIP_DEPARTURE_MAX_LENGTH;

    /*
     * Oncard Anweisungen
     */

    public static final CommandHeader SET_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00, (byte) 0x00, TICKET_MAX_SIZE, CmdType.LC_NoLE);

    public static final CommandHeader GET_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TICKET_MAX_SIZE);

    public static final CommandHeader GET_PREV_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x40, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TICKET_MAX_SIZE);

    public static final CommandHeader START_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xA0, (byte) 0x00, (byte) 0x00, TRIP_MAX_SIZE, CmdType.LC_NoLE);

    public static final CommandHeader FINISH_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xB0, (byte) 0x00, (byte) 0x00, CmdType.NoLC_NoLE);

    public static final CommandHeader GET_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xC0, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TRIP_MAX_SIZE);

    /*
     * Oncard Error-Codes
     */

}
