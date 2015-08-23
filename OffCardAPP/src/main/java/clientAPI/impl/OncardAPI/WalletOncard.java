package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

/**
 * Schnittstelle zur Low-Level Bekanntmachung der Instruktionen und
 * Fehlercodes des Geldbörsen-Applets.
 *
 */
public interface WalletOncard {

    /**
     * OnCard AID
     */
    public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'W', 'a', 'l', 'l', 'e', 't' };
    
    
    /* -------------------- OnCard Anweisungen ------------------------------------ */
    
    /**
     * APDU für Hinzufügen von Geldbetrages. Es wird stets die feste Größe eines
     * shorts erwartet.
     */
    public final static CommandHeader ADD_MONEY = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);

    /**
     * APDU für entfernen eines Geldbetrages aus der Geldbörse. Es wird stets
     * die feste Größe eines shorts erwartet.
     */
    public final static CommandHeader SUB_MONEY = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);

    /**
     * APDU für Abfrage des Bonuspunktekontostandes. Es wird stets die feste
     * Größe eines shorts geliefert. 
     */
    public final static CommandHeader CHECK_BALANCE = new CommandHeader((byte) 0xE0, (byte) 0x30, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, (short) 2);
    
    
    /* -------------------- OnCard Error-Codes ------------------------------------ */
    
    /**
     * Signalisiert, dass der Kontostand zu gering für den geforderten
     * Zahlbetrag ist.
     */
    public final static short ERROR_INSUFFICIENT_BALANCE = 0x6A10;

    /**
     * Signalisiert, dass der Kontostand bei Hinzufügen des Geldbetrages das
     * Maximum überschreiten würde.
     */
    public final static short ERROR_TRANS_EXCEED_MAXIMUM_BALANCE = 0x6A11;
}
