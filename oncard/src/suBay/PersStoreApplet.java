package suBay;

import javacard.framework.Applet;
import javacard.framework.ISOException;
import javacard.framework.ISO7816;
import javacard.framework.APDU;
import javacard.framework.Util;
import javacardx.apdu.ExtendedLength;;

public class PersStoreApplet extends Applet
{
	final static byte PERSONALDATA_CLA =  (byte) 0xE0;
	
	final static byte PERSONALDATA_INS_SET_FIRSTNAME = (byte) 0x1a;
	final static byte PERSONALDATA_INS_GET_FIRSTNAME = (byte) 0x1b;
	final static byte PERSONALDATA_INS_SET_SURNAME = (byte) 0x2a;
	final static byte PERSONALDATA_INS_GET_SURNAME = (byte) 0x2b;
	final static byte PERSONALDATA_INS_SET_BDAY = (byte) 0x3a;
	final static byte PERSONALDATA_INS_GET_BDAY = (byte) 0x3b;
	final static byte PERSONALDATA_INS_SET_LOCATION = (byte) 0x4a;
	final static byte PERSONALDATA_INS_GET_LOCATION = (byte) 0x4b;
	final static byte PERSONALDATA_INS_SET_STREET = (byte) 0x5a;
	final static byte PERSONALDATA_INS_GET_STREET = (byte) 0x5b;
	final static byte PERSONALDATA_INS_SET_PHONE = (byte) 0x6a;
	final static byte PERSONALDATA_INS_GET_PHONE  = (byte) 0x6b;
	final static byte PERSONALDATA_INS_SET_PIC = (byte) 0x7a;
	final static byte PERSONALDATA_INS_GET_PIC  = (byte) 0x7b;

	static PersDataStore personaldata;

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new PersStoreApplet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
		personaldata = new PersDataStore();
	}

	public void process(APDU apdu){
		if (selectingApplet()){
			return;
		}

		byte[] buf = apdu.getBuffer();
		short lc = (short)(buf[ISO7816.OFFSET_LC] & (short) 0x00FF);
		switch (buf[ISO7816.OFFSET_INS]){
			
		case (byte) PERSONALDATA_INS_SET_FIRSTNAME:
			trimAndSet(buf, personaldata.getFName());
			break;
			
		case (byte) PERSONALDATA_INS_GET_FIRSTNAME:
			send(apdu, personaldata.getFName());
			break;
			
		case (byte) PERSONALDATA_INS_SET_SURNAME:
			trimAndSet(buf, personaldata.getSurName());
			break;
			
		case (byte) PERSONALDATA_INS_GET_SURNAME:
			send(apdu, personaldata.getSurName());
			break;
			
		case (byte) PERSONALDATA_INS_SET_BDAY:
			trimAndSet(buf, personaldata.getBDay());
			break;
			
		case (byte) PERSONALDATA_INS_GET_BDAY:
			send(apdu, personaldata.getBDay());
			break;
			
		case (byte) PERSONALDATA_INS_SET_LOCATION: 
			trimAndSet(buf, personaldata.getLocation());
			break;
			
		case (byte) PERSONALDATA_INS_GET_LOCATION:
			send(apdu, personaldata.getLocation());
			break;
			
		case (byte) PERSONALDATA_INS_SET_STREET:
			trimAndSet(buf, personaldata.getStreet());
			break;
			
		case (byte) PERSONALDATA_INS_GET_STREET:
			send(apdu, personaldata.getStreet());
			break;
			
		case (byte) PERSONALDATA_INS_SET_PHONE:
			trimAndSet(buf, personaldata.getPhoneNumber());
			break;
			
		case (byte) PERSONALDATA_INS_GET_PHONE:
			send(apdu, personaldata.getPhoneNumber());
			break;
			
			
			
		case (byte) PERSONALDATA_INS_SET_PIC:
			
			short read = apdu.setIncomingAndReceive();
			short footage = apdu.getIncomingLength();
			
			personaldata.setPicPart(buf, (short)(ISO7816.OFFSET_EXT_CDATA), (short) 0, read);
			
			while(read < footage) {
				short read_now = apdu.receiveBytes((short) 0);
				personaldata.setPicPart(buf, (short) 0, read, read_now);
				read += read_now;
			}
			break;
			
		case (byte) PERSONALDATA_INS_GET_PIC:
			byte [] pic = personaldata.getPic();
			send(apdu, pic);
			break;
	
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
		
		
	}

	public void trimAndSet(byte[] src, byte[] des){
		short length = (short) des.length;
		Util.arrayCopy(src, (short)((ISO7816.OFFSET_CDATA) & 0x00FF), des, (short) 0, length);
	}
	
	public void send(APDU apdu, byte[] toSend){
		short length = (short) toSend.length;
		apdu.setOutgoing();
		apdu.setOutgoingLength(length);   
		apdu.sendBytesLong(toSend, (short)0, length);
	}

}
