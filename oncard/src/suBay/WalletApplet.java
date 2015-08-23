package suBay;

import javacard.framework.*;

/**
 * Applet fuer die Verwaltung der Geldboerse.
 *
 */
public class WalletApplet extends Applet {

	/* -------------------- Konstanten ------------------------------------ */
	
	/**
	 * Interner Schluessel fuer Berechtigungspruefung
	 */
	private final static byte appKey = (byte)0x01; 

	/**
	 * Initialisierungswert fuer Geldboerse.
	 */
	private final static short START_VALUE = 0;


	 /* -------------------- Anweisungen ------------------------------------ */
	 
    final static byte CLA = (byte)0xE0;
    
    final static byte INS_ADD_MONEY = (byte)0x10;
    final static byte INS_SUB_MONEY = (byte)0x20;
    final static byte INS_CHECK_BALANCE = (byte)0x30;


    /* -------------------- Error-Codes ------------------------------------ */
    
    /**
     * Signalisiert, dass der Kontostand zu gering für den geforderten Zahlbetrag ist.
     */
    public final static short ERROR_INSUFFICIENT_BALANCE = 0x6A10;

    /**
     * Signalisiert, dass der Kontostand bei Hinzufügen des Geldbetrages das Maximum überschreiten würde.
     */
    public final static short ERROR_TRANS_EXCEED_MAXIMUM_BALANCE = 0x6A11;


    /* -------------------- Membervariablen ------------------------------------ */
    
    /**
     * Geldspeicher
     */
    private ShortStore moneyc;


	/* -------------------- Methoden ------------------------------------ */
	
	/**
     * Konstruktor
     */
    private WalletApplet() {
		moneyc = new ShortStore(START_VALUE);
    }
    
	// override
    public static void install(byte[] bArray, short bOffset, byte bLength) {
		new WalletApplet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }
    
	// override
    public void process(APDU apdu) {
		if (selectingApplet()) {
			return;
		}
		byte[] buffer = apdu.getBuffer();
		if (buffer[ISO7816.OFFSET_CLA] == CLA) {
			CardHelper.checkPermission(appKey);
			
			switch (buffer[ISO7816.OFFSET_INS]) {
			case INS_CHECK_BALANCE:
				Util.setShort(buffer, (short)0, moneyc.getValue());
				apdu.setOutgoingAndSend((short)0, (short)2);
				break;
			case INS_ADD_MONEY:
				apdu.setIncomingAndReceive();
				if (!moneyc.addValue(Util.getShort(buffer, (short)ISO7816.OFFSET_CDATA))) {
					ISOException.throwIt(ERROR_TRANS_EXCEED_MAXIMUM_BALANCE);
				}
				break;
			case INS_SUB_MONEY:
				apdu.setIncomingAndReceive();
				if (!moneyc.subValue(Util.getShort(buffer, (short)ISO7816.OFFSET_CDATA))) {
					ISOException.throwIt(ERROR_INSUFFICIENT_BALANCE);
				}
				break;
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
			}
		} else {
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		}
    }
}