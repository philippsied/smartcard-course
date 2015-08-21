package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

public interface CryptoMgrOncard {

    /**
     * Oncard AID
     */
    public final static byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'C', 'r', 'y', 'p', 't', 'o', 'M', 'g', 'r' };

    public final static short CHALLENGE_SIZE = 64;

    public final static short RSA_KEY_SIZE_IN_BITS = 2048;

    /*
     * Bevor die Karte Kommandos annimmt, muss die Karte einmalig initialisiert
     * werden. Dabei wird die Karten-ID sowie der oeffentliche Schluessel des
     * Kartenausstellers festgesetzt.
     */

    /**
     * Im Lebenszyklus der Smartcard einmalig ausfuehrbar, um Karten-ID und
     * oeffentlichen Schlüssel der Terminals zu setzen
     */
    public final static CommandHeader INS_INITIALIZE = new CommandHeader((byte) 0xE0, (byte) 0xEE, (byte) 0x00,
	    (byte) 0x00, CmdType.LC_NoLE);

    public final static CommandHeader INS_GET_CARD_ID = new CommandHeader((byte) 0xE0, (byte) 0x10, (byte) 0x00,
	    (byte) 0x00, CmdType.NoLC_LE, (short) 4);

    public final static CommandHeader INS_GET_TRUSTED_PUBKEY = new CommandHeader((byte) 0xE0, (byte) 0x20, (byte) 0x00,
	    (byte) 0x00, CmdType.NoLC_LE);

    public final static CommandHeader INS_START_CHALLENGE_RESPONSE = new CommandHeader((byte) 0xE0, (byte) 0xA0,
	    (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE, (short) 64);

    public final static CommandHeader INS_FINISH_CHALLENGE_RESPONSE = new CommandHeader((byte) 0xE0, (byte) 0xB0,
	    (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    /*
     * Fehlercodes
     * 
     */

    public final static short ERROR_CARD_NOT_INITIALIZED = 0x6A80;
    public final static short ERROR_CARD_ALREADY_INITIALIZED = 0x6A81;
    public final static short ERROR_INSUFFICIENT_PERMISSIONS = 0x6A6A;
}
