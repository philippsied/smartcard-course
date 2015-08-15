package suBay;

import javacard.framework.*;

public class WalletApplet extends Applet {

	final static byte CLA =  (byte) 0xE0;
    final static byte INS_ADD_MONEY = (byte) 0x10; 
    final static byte INS_SUB_MONEY = (byte) 0x20; 
	final static byte INS_CHECK_BALANCE = (byte) 0x30; 
	
	final static short START_VALUE = 0;
	
	final static short ERROR_INSUFFICIENT_BALANCE = 0x6A83;
	final static short ERROR_TRANS_EXCEED_MAXIMUM_BALANCE = 0x6A84;
	
	private ShortStore moneyc;
	
	private WalletApplet() {
		moneyc = new ShortStore(START_VALUE);
	}

	public static void install(byte[] bArray, short bOffset, byte bLength) {
		new WalletApplet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu) {	
		if (selectingApplet()) {
			return;
		}

		byte[] buffer = apdu.getBuffer();
		if (buffer[ISO7816.OFFSET_CLA] == CLA) {
			switch (buffer[ISO7816.OFFSET_INS]) {			
			case INS_CHECK_BALANCE:
				Util.setShort(buffer, (short) 0, moneyc.getValue());
				apdu.setOutgoingAndSend((short) 0, (short) 2);  
				break; 		
			case INS_ADD_MONEY: 
				apdu.setIncomingAndReceive();
				if (!moneyc.addValue(Util.getShort(buffer, (short) ISO7816.OFFSET_CDATA))) {
					CardRuntimeException.throwIt(ERROR_TRANS_EXCEED_MAXIMUM_BALANCE);
				}
				break; 
			case INS_SUB_MONEY: 
				apdu.setIncomingAndReceive();
				if (moneyc.subValue(Util.getShort(buffer, (short) ISO7816.OFFSET_CDATA))) {
					CardRuntimeException.throwIt(ERROR_INSUFFICIENT_BALANCE);
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
