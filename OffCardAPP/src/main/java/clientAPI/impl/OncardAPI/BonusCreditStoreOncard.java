package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

public interface BonusCreditStoreOncard {

    /**
     * Oncard AID
     */
    public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'B', 'o', 'n', 'u', 's', 'C', 'r', 'e', 'd', 'i', 't' };

    /**
     * Add bonus credits to store
     * 
     */
    public final static CommandHeader ADD_CREDITS = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);

    /**
     * APDU-Command to remove bonus credits from store
     * 
     */
    public final static CommandHeader SUB_CREDITS = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);

    /**
     * Get current amount of bonus credits from store
     * 
     */
    public final static CommandHeader CHECK_BALANCE = new CommandHeader((byte) 0xE0, (byte) 0x30, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, (short) 2);

    /*
     * Oncard Konstanten
     */

    
    
    /*
     * Oncard Error-Codes
     */

    public final static short ERROR_INSUFFICIENT_BALANCE = 0x6A83;
    public final static short ERROR_TRANS_EXCEED_MAXIMUM_BALANCE = 0x6A84;
}
