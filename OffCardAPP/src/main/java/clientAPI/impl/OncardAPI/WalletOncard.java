package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

public interface WalletOncard {
   
    /**
     * Oncard AID
     */
    public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'W', 'a', 'l', 'l', 'e', 't' };

    
    /*
     * Oncard Anweisungen
     */
    
    public final static CommandHeader ADD_MONEY = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);

    public final static CommandHeader SUB_MONEY = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);

    public final static CommandHeader CHECK_BALANCE = new CommandHeader((byte) 0xE0, (byte) 0x30, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, (short) 2);

    
    /*
     * Oncard Konstanten
     */
    
    
    /*
     * Oncard Error-Codes
     */
    
    
}
