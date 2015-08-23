package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

/**
 * Schnittstelle zur Low-Level Bekanntmachung der Instruktionen und
 * Fehlercodes des Bonuspunkte-Applets.
 *
 */
public interface BonusCreditStoreOncard {

    /**
     * OnCard AID
     */
    public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'B', 'o', 'n', 'u', 's', 'C', 'r', 'e', 'd',  'i', 't' };

    
    /* -------------------- OnCard Anweisungen ------------------------------------ */
    
    /**
     * APDU für Hinzufügen von Bonuspunkten. Es wird stets die feste Größe eines
     * shorts erwartet.
     */
    public final static CommandHeader ADD_CREDITS = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00,  (byte) 0x00, (short) 2, CmdType.LC_NoLE);

    /**
     * APDU für entfernen von Bonuspunkten. Es wird stets die feste Größe eines
     * shorts erwartet.
     */
    public final static CommandHeader SUB_CREDITS = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, (short) 2, CmdType.LC_NoLE);

    /**
     * APDU für Abfrage des Bonuspunktekontostandes. Es wird stets die feste
     * Größe eines shorts geliefert.
     */
    public final static CommandHeader CHECK_BALANCE = new CommandHeader((byte) 0xE0, (byte) 0x30, (byte) 0x00,  (byte) 0x00, CmdType.NoLC_LE, (short) 2);

    
    /* -------------------- OnCard Error-Codes ------------------------------------ */

    /**
     * Signalisiert, dass der Kontostand zu gering für den geforderten Zahlbetrag ist.
     */
    public final static short ERROR_INSUFFICIENT_BALANCE = 0x6A20;
    
    /**
     * Signalisiert, dass der Kontostand bei Hinzufügen der Bonuspunkte das Maximum überschreiten würde.
     */
    public final static short ERROR_TRANS_EXCEED_MAXIMUM_BALANCE = 0x6A21;
}
