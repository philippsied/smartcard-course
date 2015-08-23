package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

public interface CryptoMgrOncard {

    /**
     * OnCard AID
     */
    public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'C', 'r', 'y', 'p', 't', 'o', 'M', 'g', 'r' };

    /* -------------------- OnCard Konstanten ------------------------------------ */

    /**
     * Die Größe der Challenge in Bytes.
     */
    public final static short CHALLENGE_SIZE = 64;

    /**
     * Die Größe des RSA-Modulus in Bits.
     */
    public final static short RSA_KEY_SIZE_IN_BITS = 2048;

    /* -------------------- OnCard Anweisungen ------------------------------------ */

    /*
     * Bevor die Karte Kommandos annimmt, muss die Karte einmalig initialisiert
     * werden. Dabei wird die Karten-ID sowie der oeffentliche Schluessel des
     * Kartenausstellers festgesetzt.
     */

    /**
     * Im Lebenszyklus der Smartcard einmalig ausfuehrbar, um Karten-ID und
     * öffentlichen Schlüssel der Terminals zu setzen
     */
    public final static CommandHeader INS_INITIALIZE = new CommandHeader((byte) 0xE0, (byte) 0xEE, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    /**
     * Liefert die Karten-ID
     */
    public final static CommandHeader INS_GET_CARD_ID = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, (short) 4);

    /**
     * Liefert den hinterlegten öffentlichen Schlüssel
     */
    public final static CommandHeader INS_GET_TRUSTED_PUBKEY = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE);

    /**
     * Startet das Challenge-Response-Protokoll durch Anfrage einer Challenge
     */
    public final static CommandHeader INS_START_CHALLENGE_RESPONSE = new CommandHeader((byte) 0xE0, (byte) 0xA0, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, (short) 64);

    /**
     * Beendet das Challenge-Repsonse-Protokoll durch Übertragung der
     * verschlüsselten Challenge (Durch privaten, zum hinterlegten öffentlichen
     * RSA-Schlüssel passenden, Schlüssel verschlüsselt)
     */
    public final static CommandHeader INS_FINISH_CHALLENGE_RESPONSE = new CommandHeader((byte) 0xE0, (byte) 0xB0, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    /* -------------------- OnCard Error-Codes ------------------------------------ */

    /**
     * Signalisiert, dass die Karte noch nicht initialisiert ist und folglich keine Befehle entgegen nimmt.
     */
    public final static short ERROR_CARD_NOT_INITIALIZED = 0x6A60;
   
    /**
     * Signalisiert, dass die Karte bereits initialisiert wurde, weshalb eine weitere Initialisierung nicht möglich ist.
     */
    public final static short ERROR_CARD_ALREADY_INITIALIZED = 0x6A61;
    
    /**
     * Signalisiert, dass die gelieferte Challenge ungültig ist.
     */
    public final static short ERROR_INVALID_CHALLENGE = 0x6A62;

    /**
     * Signalisiert, dass keine Berechtigung für den Zugriff auf die Applet-Funktionalitäten existiert.
     */
    public final static short ERROR_INSUFFICIENT_PERMISSIONS = 0x6A6A;
}
