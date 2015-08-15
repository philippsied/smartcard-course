package suBay;

import javacard.framework.*;

public class TicketMgrApplet extends Applet {
	
	final static byte CLA =  (byte) 0xE0;
    final static byte INS_SET_TICKET = (byte) 0x10;
    final static byte INS_GET_TICKET = (byte) 0x20; 
	final static byte INS_EXPIRE_TICKET = (byte) 0x30; 
	final static byte INS_GET_PREV_TICKET = (byte) 0x40; 
	
	final static byte INS_START_TRIP = (byte) 0xA0;  
	final static byte INS_FINISH_TRIP = (byte) 0xB0;
	final static byte INS_GET_TRIP = (byte) 0xC0;
	
	final static byte MAX_TICKET_DESCRIPTION_LENGTH = (byte) 30;
	final static byte MAX_TRIP_DEPARTURE_LENGTH = (byte) 30;
	
	private Ticket currentTicket;
	private Ticket previousTicket;
	private Trip start;
	
	private TicketMgrApplet(){
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
				 
				break; 	
			case INS_GET_TICKET: 

				break; 	
			case INS_EXPIRE_TICKET: 
					
				break;
			case INS_GET_PREV_TICKET:
				 
				break; 	
			case INS_START_TRIP: 

				break; 	
			case INS_FINISH_TRIP: 
					
				break;
			case INS_GET_TRIP: 
					
				break; 
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
			}  
	    } else {
		    ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
	    }	
	}
}
