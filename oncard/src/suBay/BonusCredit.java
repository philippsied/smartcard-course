package suBay;

import javacard.framework.*;

public class BonusCredit extends Applet
{

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new BonusCredit().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

    final static byte CLA = (byte) 0xE0; 
    final static byte INS_ADD_CREDITS = (byte) 0x10; 
    final static byte INS_REM_CREDITS = (byte) 0x20; 
	final static byte INS_CHECK_BALANCE = (byte) 0x30;
	
	final static short START_VALUE = 0; 
	
	private ShortStore balance;
	private BonusCreditStoreApplet thisCreditStore;
	
	private BonusCreditStoreApplet()
	{
		this.balance = new ShortStore(START_VALUE);
	}

	//overide
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		BonusCreditStoreApplet thisCreditStore = new BonusCreditStoreApplet();
		thisCreditStore.register();
	}
	
	//overide
	public boolean select() 
    { 
		return true; 
    }
	
	//overide
	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}
		byte[] buffer = apdu.getBuffer(); 
		if (buffer[ISO7816.OFFSET_CLA] == CLA)  
	    { 
			switch(buffer[ISO7816.OFFSET_INS])
			{
			case INS_ADD_CREDITS:
						handleAdd(apdu,buffer);
						break;
			case INS_REM_CREDITS:
						handleRemove(apdu,buffer);
						break;
			case INS_CHECK_BALANCE: 
						handleCheck(apdu,buffer);
						break;					
			default:
						ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);	
			}
		}
	}

	private void handleAdd(APDU apdu, byte[] buffer) {
		byte lc = buffer[ISO7816.OFFSET_LC];
		short readBytes = apdu.setIncomingAndReceive(); 
		sendAck(apdu, buffer, this.balance.addValue(Util.getShort(buffer, (short) ISO7816.OFFSET_CDATA)));
	}

	private void handleRemove(APDU apdu, byte[] buffer) {
		byte lc = buffer[ISO7816.OFFSET_LC];
		short readBytes = apdu.setIncomingAndReceive(); 
		sendAck(apdu, buffer, removeBonusCredits(Util.getShort(buffer, (short) ISO7816.OFFSET_CDATA)));
	}
	
		
	private void handleCheck(APDU apdu, byte[] buffer) {
		Util.setShort(buffer, (short) 0, this.balance);
		apdu.setOutgoingAndSend((short) 0, (short) buffer[ISO7816.OFFSET_LC]);
	}
}
