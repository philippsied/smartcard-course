package suBay;

import javacard.framework.*;

/**
 * Applet fuer die Verwaltung der Bonuspunkte.
 *
 */
public class BonusCreditApplet extends Applet {

	/* -------------------- Konstanten ------------------------------------ */
	
	/**
	 * Interner Schluessel fuer Berechtigungspruefung
	 */
	private final static byte appKey = (byte)0x02; 
	
	/**
	 * Initialisierungswert fuer Geldboerse.
	 */
	private final static short START_VALUE = 0;
	
	
	/* -------------------- Anweisungen ------------------------------------ */
	
    final static byte CLA = (byte)0xE0;
    
    final static byte INS_ADD_CREDITS = (byte)0x10;
    final static byte INS_SUB_CREDITS = (byte)0x20;
    final static byte INS_CHECK_BALANCE = (byte)0x30;


    /* -------------------- Error-Codes ------------------------------------ */

    /**
     * Signalisiert, dass der Kontostand zu gering für den geforderten Zahlbetrag ist.
     */
    public final static short ERROR_INSUFFICIENT_BALANCE = 0x6A20;
    
    /**
     * Signalisiert, dass der Kontostand bei Hinzufuegen der Bonuspunkte das Maximum ueberschreiten wuerde.
     */
    public final static short ERROR_TRANS_EXCEED_MAXIMUM_BALANCE = 0x6A21;

	
	/* -------------------- Membervariablen ------------------------------------ */
	
	/**
     * Bonuspunktespeicher
     */
    private ShortStore balance;
    
    
	/* -------------------- Methoden ------------------------------------ */
	
	/**
     * Konstruktor
     */
    private BonusCreditApplet() {
		balance = new ShortStore(START_VALUE);
    }

    // override
    public static void install(byte[] bArray, short bOffset, byte bLength) {
		new BonusCreditApplet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }

    // override
    public boolean select() {
		return true;
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
			case INS_ADD_CREDITS:
				handleAdd(apdu, buffer);
				break;
			case INS_SUB_CREDITS:
				handleRemove(apdu, buffer);
				break;
			case INS_CHECK_BALANCE:
				handleCheck(apdu, buffer);
				break;
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
			}
		} else {
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		}
    }

    private void handleAdd(APDU apdu, byte[] buffer) {
		checkLength(apdu,buffer);
		if (!this.balance.addValue(Util.getShort(buffer, (short)ISO7816.OFFSET_CDATA))) {
			ISOException.throwIt(ERROR_TRANS_EXCEED_MAXIMUM_BALANCE);
		}
    }

    private void handleRemove(APDU apdu, byte[] buffer) {
		checkLength(apdu,buffer);
		if (!this.balance.subValue(Util.getShort(buffer, (short)ISO7816.OFFSET_CDATA))) {
			ISOException.throwIt(ERROR_INSUFFICIENT_BALANCE);
		}
    }

	private void checkLength(APDU apdu, byte[] buffer) {
		byte lc = buffer[ISO7816.OFFSET_LC];
		short readBytes = apdu.setIncomingAndReceive();
		if ((short)lc != readBytes) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
	}

    private void handleCheck(APDU apdu, byte[] buffer) {
		Util.setShort(buffer, (short)0, this.balance.getValue());
		apdu.setOutgoingAndSend((short)0, (short)2);
    }
}
