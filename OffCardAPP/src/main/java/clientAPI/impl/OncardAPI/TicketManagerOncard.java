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

    public final static byte TICKET_DESCRIPTION_LENGTH = (byte) 30;
    public final static byte TICKET_SIZE = (byte) (Short.BYTES + 2 * Byte.BYTES) + TICKET_DESCRIPTION_LENGTH;
    public final static byte TRIP_DEPARTURE_LENGTH = (byte) 30;
    public final static byte TRIP_SIZE = (byte) Short.BYTES + TRIP_DEPARTURE_LENGTH;

    /*
     * Oncard Anweisungen
     */

    public final static CommandHeader SET_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00, (byte) 0x00, TICKET_SIZE, CmdType.LC_NoLE);

    public final static CommandHeader GET_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TICKET_SIZE);

//    public final static CommandHeader EXPIRE_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x30, (byte) 0x00, (byte) 0x00, CmdType.NoLC_NoLE);

    public final static CommandHeader GET_PREV_TICKET = new CommandHeader((byte) 0xE0, (byte) 0x40, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TICKET_SIZE);

    public final static CommandHeader START_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xA0, (byte) 0x00, (byte) 0x00, TRIP_SIZE, CmdType.LC_NoLE);

    public final static CommandHeader FINISH_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xB0, (byte) 0x00, (byte) 0x00, CmdType.NoLC_NoLE);

    public final static CommandHeader GET_TRIP = new CommandHeader((byte) 0xE0, (byte) 0xC0, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, TRIP_SIZE);

    /*
     * Oncard Error-Codes
     */

}
