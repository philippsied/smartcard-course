package suBay;

import javacard.framework.*;

public class TicketMgrApplet extends Applet {

    final static byte CLA = (byte) 0xE0;
    final static byte INS_SET_TICKET = (byte) 0x10;
    final static byte INS_GET_TICKET = (byte) 0x20;
    // final static byte INS_EXPIRE_TICKET = (byte) 0x30;
    final static byte INS_GET_PREV_TICKET = (byte) 0x40;

    final static byte INS_START_TRIP = (byte) 0xA0;
    final static byte INS_FINISH_TRIP = (byte) 0xB0;
    final static byte INS_GET_TRIP = (byte) 0xC0;

    final static byte TICKET_DESCRIPTION_LENGTH = (byte) 30;
    // 4 = Bytes for duration, durationUnit, startValidityTS
    final static byte TICKET_SIZE = (byte) 8 + TICKET_DESCRIPTION_LENGTH;

    final static byte TRIP_DEPARTURE_LENGTH = (byte) 30;
 // 4 = Bytes for startTS
    final static byte TRIP_SIZE = (byte) 4 + TRIP_DEPARTURE_LENGTH;

    /*
     * Error-Codes
     */

    /**
     * 
     */
    final static short ERROR_PENDING_TICKET = 0x6A83;

    private Ticket currentTicket;
    private Ticket previousTicket;
    private Trip start;

    private TicketMgrApplet() {
	currentTicket = null;
	previousTicket = null;
	start = null;
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
	new TicketMgrApplet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }

    public void process(APDU apdu) {
	if (selectingApplet()) {
	    return;
	}

	byte[] buffer = apdu.getBuffer();
	if (buffer[ISO7816.OFFSET_CLA] == CLA) {
	    switch (buffer[ISO7816.OFFSET_INS]) {
	    case INS_SET_TICKET:
		handleSetTicket(apdu, buffer);
		break;
	    case INS_GET_TICKET:
		handleGetTicket(apdu, buffer);
		break;
	    case INS_GET_PREV_TICKET:
		handleGetPreviousTicket(apdu, buffer);
		break;
	    case INS_START_TRIP:
		handleStartTrip(apdu, buffer);
		break;
	    case INS_FINISH_TRIP:
		handleFinishTrip(apdu, buffer);
		break;
	    case INS_GET_TRIP:
		handleGetTrip(apdu, buffer);
		break;
	    default:
		ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
	    }
	} else {
	    ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
	}
    }

    private void handleSetTicket(APDU apdu, byte[] buffer) {
	checkLengthAndReceive(apdu, buffer, TICKET_SIZE);

	try {
	    this.previousTicket = this.currentTicket;
	    final short tmpStartTS_h = Util.getShort(buffer, (short) ISO7816.OFFSET_CDATA);
	    final short tmpStartTS_l = Util.getShort(buffer, (short) (ISO7816.OFFSET_CDATA + 2));
	    final short tmpExpTS_h = Util.getShort(buffer, (short) (ISO7816.OFFSET_CDATA + 4));
	    final short tmpExpTS_l = Util.getShort(buffer, (short) (ISO7816.OFFSET_CDATA + 6));
	    final byte[] tmpDescription = new byte[TICKET_DESCRIPTION_LENGTH];
	    Util.arrayCopy(buffer, (short) (ISO7816.OFFSET_CDATA + 8), tmpDescription, (short) 0,
		    (short) tmpDescription.length);
	    this.currentTicket = new Ticket(tmpStartTS_h, tmpStartTS_l, tmpExpTS_h, tmpExpTS_l, tmpDescription);
	} catch (Exception e) {
	    ISOException.throwIt(ISO7816.SW_UNKNOWN);
	}
    }

    private void handleGetTicket(APDU apdu, byte[] buffer) {
	if (this.currentTicket != null) {
	    this.currentTicket.fillBuffer(buffer);
	    apdu.setOutgoingAndSend((short) 0, (short) TICKET_SIZE);
	} else {
	    apdu.setOutgoingAndSend((short) 0, (short) 0);
	}
    }

    private void handleGetPreviousTicket(APDU apdu, byte[] buffer) {
	if (this.previousTicket != null) {
	    this.previousTicket.fillBuffer(buffer);
	    apdu.setOutgoingAndSend((short) 0, (short) TICKET_SIZE);
	} else {
	    apdu.setOutgoingAndSend((short) 0, (short) 0);
	}
    }

    private void handleStartTrip(APDU apdu, byte[] buffer) {
	checkLengthAndReceive(apdu, buffer, TRIP_SIZE);
	if (this.start != null) {
	    ISOException.throwIt(ERROR_PENDING_TICKET);
	}
	try {
	    final short tmpStartTS_h = Util.getShort(buffer, (short) ISO7816.OFFSET_CDATA);
	    final short tmpStartTS_l = Util.getShort(buffer, (short) (ISO7816.OFFSET_CDATA+2));
	    final byte[] tmpDeparture = new byte[TRIP_DEPARTURE_LENGTH];
	    Util.arrayCopy(buffer, (short) (ISO7816.OFFSET_CDATA + 4), tmpDeparture, (short) 0,
		    (short) tmpDeparture.length);
	    this.start = new Trip(tmpStartTS_h, tmpStartTS_l, tmpDeparture);
	} catch (Exception e) {
	    ISOException.throwIt(ISO7816.SW_UNKNOWN);
	}

    }

    private void handleFinishTrip(APDU apdu, byte[] buffer) {
	this.start = null;
	JCSystem.requestObjectDeletion();
    }

    private void handleGetTrip(APDU apdu, byte[] buffer) {
	if (this.start != null) {
	    this.start.fillBuffer(buffer);
	    apdu.setOutgoingAndSend((short) 0, (short) TRIP_SIZE);
	} else {
	    apdu.setOutgoingAndSend((short) 0, (short) 0);
	}
    }

    private byte checkLengthAndReceive(APDU apdu, byte[] buffer, byte limit) {
	final byte lc = buffer[ISO7816.OFFSET_LC];

	if (lc > limit) {
	    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
	}
	final short readBytes = apdu.setIncomingAndReceive();
	if (lc != readBytes) {
	    ISOException.throwIt(ISO7816.SW_UNKNOWN);
	}
	return lc;
    }
}
