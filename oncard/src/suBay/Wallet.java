package suBay;

import javacard.framework.*;

public class Wallet extends Applet
{

	final static byte CLA =  (byte) 0xE0;
    final static byte INS_ADD_MONEY = (byte) 0x10; 
    final static byte INS_REM_MONEY = (byte) 0x20; 
	final static byte INS_CHECK_BALANCE = (byte) 0x30; 
	
	final static short START_VALUE = 0;
	
	private ShortStore moneyc;

	public static void install(byte[] bArray, short bOffset, byte bLength) {
		new Wallet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
		moneyc = new ShortStore(START_VALUE);
	}

	public void process(APDU apdu){	
		if (selectingApplet()){
			return;
		}

		byte[] buffer = apdu.getBuffer();
		if (buffer[ISO7816.OFFSET_CLA] == CLA)  
	    {
			switch (buffer[ISO7816.OFFSET_INS]){
						
			case (byte) INS_CHECK_BALANCE:
				Util.setShort(buffer, (short) 0, moneyc.getMoney());
				apdu.setOutgoingAndSend((short) 0, (short) ISO7816.OFFSET_P1);  
				break; 
				
			case (byte) INS_ADD_MONEY: 
				apdu.setIncomingAndReceive();
				moneyc.addMoney(Util.getShort(buffer, (short) ISO7816.OFFSET_CDATA));
				break; 
				
			case (byte) INS_REM_MONEY: 
				apdu.setIncomingAndReceive();
				moneyc.subMoney(Util.getShort(buffer, (short) ISO7816.OFFSET_CDATA));
				break; 
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
			}  
	    }	
	}
}
