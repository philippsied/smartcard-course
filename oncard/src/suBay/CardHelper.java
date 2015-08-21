package suBay;

import javacard.framework.JCSystem;
import javacard.framework.Shareable;
import javacard.framework.ISOException;
import javacard.framework.AID;

public class CardHelper {
	
	public final static short DT_SHORT_SIZE_BYTES = (short) 2;
	public final static short DT_SHORT_SIZE_BITS = (short) 16;
	
	private final static byte[] CRYPTO_MGR_AID =  { (byte) 0xFD, 'u', 'B', 'a', 'y', 'C', 'r', 'y', 'p', 't', 'o', 'M', 'g', 'r' };
	
	
	public final static short ERROR_INSUFFICIENT_PERMISSIONS = 0x6A6A;
	
	public static void checkPermission(byte appKey){
	    AID cryptoAID = JCSystem.lookupAID(CRYPTO_MGR_AID, (short)0, (byte)CRYPTO_MGR_AID.length);
		CryptoMgr cmgr = (CryptoMgr) JCSystem.getAppletShareableInterfaceObject(cryptoAID, (byte) 0);
		if(!cmgr.isAuthorizedFor(appKey)){
			ISOException.throwIt(ERROR_INSUFFICIENT_PERMISSIONS);
		}
    }
}
