package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

public interface BonusCreditStoreOncard {
	public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'B', 'o', 'n', 'u', 's', 'C', 'r', 'e', 'd', 'i', 't' };
		
	/**
	 * Add bonus credits to store <br>
	 * <pre>
	 * CLA: 0xE0 <br>
	 * INS: 0x00 <br>
	 * P1: 0x00 <br>
	 * P2: 0x00 <br>
	 * LC: 0x02 (fix) <br>
	 * </pre>
	 */
	public final static CommandHeader ADD_CREDITS = new CommandHeader( (byte) 0xE0, (byte) 0x10, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);
	
	/**
	 * APDU-Command to remove bonus credits from store <br>
	 * <pre>
	 * CLA: 0xE0 <br>
	 * INS: 0x02 <br>
	 * P1: 0x00 <br>
	 * P2: 0x00 <br>
	 * LC: 0x02 (fix) <br>
	 * </pre>
	 */
	public final static CommandHeader REM_CREDITS = new CommandHeader( (byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);
	
	/**
	 * Get current amount of bonus credits from store <br>
	 * <pre>
	 * CLA: 0xE0 <br>
	 * INS: 0x04 <br>
	 * P1: 0x00 <br>
	 * P2: 0x00 <br>
	 * LE: 0x02 (fix) <br>
	 * </pre>
	 */
	public final static CommandHeader CHECK_BALANCE =  new CommandHeader( (byte) 0xE0, (byte) 0x30, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, (short) 2);
	
	
	// Place for error codes
	
	public final static short ERROR_INSUFFICIENT_AMOUNT = 0x6A83; 
	public final static short ERROR_TRANS_EXCEED_MAXIMUM_AMOUNT = 0x6A84;
}
