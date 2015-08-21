package suBay;

import javacard.framework.*;
import javacard.security.KeyPair;
import javacard.security.RSAPrivateCrtKey;
import javacard.security.RSAPublicKey;
import javacard.security.AESKey;
import javacard.security.KeyBuilder;
import javacard.security.CryptoException;
import javacard.security.RandomData;
import javacardx.crypto.Cipher;
import javacardx.apdu.ExtendedLength;

public class CryptoMgrApplet extends Applet implements CryptoMgr, ExtendedLength {

     
 /* --------------------------------------------------------------*/    
     /*
      * Bevor die Karte Kommandos annimmt, muss die Karte einmalig initialisiert werden. Dabei wird die Karten-ID sowie der oeffentliche Schluessel des Kartenausstellers festgesetzt.
      */
     	
	
	final static byte CLA = (byte)0xE0;
	
	
	/**
     * Im Lebenszyklus der Smartcard einmalig ausfuehrbar, um Karten-ID und oeffentlichen EC-Schluessel der Terminals zu setzen
     */
    final static byte INS_INITIALIZE = (byte)0xEE;
    
    
	final static byte INS_GET_CARD_ID = (byte)0x10;
	final static byte INS_GET_TRUSTED_PUBKEY = (byte)0x20;
	final static byte INS_START_CHALLENGE_RESPONSE = (byte)0xA0;
	final static byte INS_FINISH_CHALLENGE_RESPONSE = (byte)0xB0;
	
/* ----------------------------------------------------------------*/	
	
	private static boolean IS_INITIALIZED = false;
	

	
	private final static short BUFFER_SIZE = 1024; 
	private final static short CHALLENGE_SIZE = 64;
	private final static short ASYM_KEY_SIZE_IN_BITS = KeyBuilder.LENGTH_RSA_2048;  
	
	/**
	 * Einmalig setzbare Karten-ID zur Registrierung beim Aussteller. Aufgeteilt in High und Low-Teil eines Integers (4-Byte Ganzzahlig) 
	 */
	private short mCardID_h = 0;
	private short mCardID_l = 0;  
	private RSAPublicKey mOffcardPublicKey;
	
	private RandomData randomGen;
	private Cipher mRecvCipher;
	
	
	/**
	 * Transientes byte-Array zur Bereitstellung eines temporaeren Puffers
	 */
	private byte[] tmpBuffer = null;
	
	
	
	/**
	 * Transientes byte-Array fuer die erzeugte challenge
	 */
	private byte[] challenge = null;
	
	 
	
	/**
	 * Transientes byte-Array zur Speicherung des Verbindungsstatus
	 */
	private byte[] mConnectionState = null;
	
	/**
	 * Die Challenge wurde an das Terminal gesendet und es wird auf die Response gewartet.
	 */ 
	final static byte WAIT_FOR_RESPONSE = (byte) 0x01;
	
	/**
	 * Die Challenge-Response-Authentifizierung wurde erfolgreich durchgefuehrt.
	 */
	final static byte CONNECTION_ESTABLISHED = (byte) 0x02;	


	final static short ERROR_CARD_NOT_INITIALIZED = 0x6A80;
	final static short ERROR_CARD_ALREADY_INITIALIZED = 0x6A81;
	final static short ERROR_INVALID_CHALLENGE = 0x6A83;



    private CryptoMgrApplet() {
		try{		
			mRecvCipher = Cipher.getInstance(Cipher.ALG_RSA_PKCS1, false);				
			mOffcardPublicKey =	(RSAPublicKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PUBLIC, ASYM_KEY_SIZE_IN_BITS, false);
			
			randomGen = RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);
		} catch(CryptoException c){    
			short reason = c.getReason();
			ISOException.throwIt(reason);
		}
    }

    // overide
    public static void install(byte[] bArray, short bOffset, byte bLength) {
	new CryptoMgrApplet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }

    // overide
    public boolean select() {
    		tmpBuffer = JCSystem.makeTransientByteArray(BUFFER_SIZE, JCSystem.CLEAR_ON_RESET);
	return true;
    }

    // overide
    public void process(APDU apdu) {
	if (selectingApplet()) {
	    return;
	}
	byte[] buffer = apdu.getBuffer();
	if (buffer[ISO7816.OFFSET_CLA] == CLA) {
		if(IS_INITIALIZED){
			switch (buffer[ISO7816.OFFSET_INS]) {
			case INS_GET_CARD_ID:
				handleGetCardId(apdu,buffer);
			break;
			case INS_GET_TRUSTED_PUBKEY:
				handleGetOffcardPublicKey(apdu);
			break;
			case INS_START_CHALLENGE_RESPONSE:
				handleStartChallengeResponse(apdu, buffer);
			break;
			case INS_FINISH_CHALLENGE_RESPONSE:
				handleFinishChallengeResponse(apdu, buffer);
			break;
			case INS_INITIALIZE:
				ISOException.throwIt(ERROR_CARD_ALREADY_INITIALIZED);
			break;
			default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
			} 
		} else {
			if(buffer[ISO7816.OFFSET_INS] == INS_INITIALIZE){
				initializeCard(apdu, buffer);
			} else {
				ISOException.throwIt(ERROR_CARD_NOT_INITIALIZED);
			}	
		}		
	}else{
		ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
	} 
			
		
	
    }
    
     private void initializeCard(APDU apdu, byte[] buffer){
		readLongAPDUBuffer(tmpBuffer,BUFFER_SIZE,apdu,buffer);
		short offset = (short) 0;
		try{
			mCardID_h = Util.getShort(tmpBuffer, offset);
			offset += CardHelper.DT_SHORT_SIZE_BYTES;
			mCardID_l = Util.getShort(tmpBuffer, offset);
			offset += CardHelper.DT_SHORT_SIZE_BYTES;
			short modulusLength = Util.getShort(tmpBuffer, offset);
			offset += CardHelper.DT_SHORT_SIZE_BYTES;
			short expLength = Util.getShort(tmpBuffer, offset);
			offset += CardHelper.DT_SHORT_SIZE_BYTES;
			try{
				mOffcardPublicKey.setModulus(tmpBuffer, offset, modulusLength);
				offset+=modulusLength;
				mOffcardPublicKey.setExponent(tmpBuffer, offset, expLength);
				
				IS_INITIALIZED = true;		
			}catch(CryptoException c){    
				short reason = c.getReason();
				ISOException.throwIt(reason);
			}
		}catch(CardRuntimeException e){
			mCardID_h = (short)0;
			mCardID_l = (short)0;
			ISOException.throwIt(e.getReason());
		}
    }
    
       
    
    private void handleGetCardId(APDU apdu, byte[] buffer){
	   	Util.setShort(buffer, (short) 0, mCardID_h);
	   	Util.setShort(buffer, (short) 2, mCardID_l);
		apdu.setOutgoingAndSend((short) 0, (short) 4); 
    }
    
    
    private void handleGetOffcardPublicKey(APDU apdu){
		short offset = (short)(2*CardHelper.DT_SHORT_SIZE_BYTES);
	   	try{
			short modulusLength = mOffcardPublicKey.getModulus(tmpBuffer, offset);
			offset += modulusLength;
			short exponentLength = mOffcardPublicKey.getExponent(tmpBuffer, offset);
			offset += exponentLength;
			Util.setShort(tmpBuffer, (short)0, modulusLength);
			Util.setShort(tmpBuffer, (short)2, exponentLength);
			apdu.setOutgoing();
			apdu.setOutgoingLength(offset);
			apdu.sendBytesLong(tmpBuffer, (short)0, offset);
	   	} catch(CryptoException c){    
			short reason = c.getReason();
			ISOException.throwIt(reason);
		}
    }  
    
    private void handleStartChallengeResponse(APDU apdu, byte[] buffer){
		challenge = JCSystem.makeTransientByteArray(CHALLENGE_SIZE, JCSystem.CLEAR_ON_RESET);
		randomGen.generateData(challenge, (short)0, CHALLENGE_SIZE);
				
		Util.arrayCopyNonAtomic(challenge, (short)0, buffer, (short)0, CHALLENGE_SIZE);
		apdu.setOutgoingAndSend((short)0, CHALLENGE_SIZE);
		
		mConnectionState = JCSystem.makeTransientByteArray((short)1, JCSystem.CLEAR_ON_RESET);
		mConnectionState[0] = WAIT_FOR_RESPONSE;
    }
    
    private void handleFinishChallengeResponse(APDU apdu, byte[] buffer){
		short size = readLongAPDUBuffer(tmpBuffer, BUFFER_SIZE, apdu, buffer);
		mRecvCipher.init(mOffcardPublicKey, Cipher.MODE_DECRYPT);
		short resultLength = mRecvCipher.doFinal(tmpBuffer, (short)0, size, tmpBuffer, size);
		
		if(0 != Util.arrayCompare(challenge, (short)0, tmpBuffer, size, CHALLENGE_SIZE)){
			ISOException.throwIt(ERROR_INVALID_CHALLENGE);
		} else{
			mConnectionState[0] = CONNECTION_ESTABLISHED;
		}
    }
    
    
    private short readLongAPDUBuffer(byte[] target, short targetMaxSize, APDU apdu, byte[] buffer){
	    short read = apdu.setIncomingAndReceive();
		short lc = apdu.getIncomingLength();
		if(lc < 256 || lc > targetMaxSize){
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
		Util.arrayCopyNonAtomic(buffer, (short)ISO7816.OFFSET_EXT_CDATA, target, (short)0, read);
		while(read < lc){
			short currentlyRead = apdu.receiveBytes((short)0);
			if(currentlyRead <= 0){
				ISOException.throwIt(ISO7816.SW_DATA_INVALID);
			}
			Util.arrayCopyNonAtomic(buffer, (short)0, target, read, currentlyRead);
			read += currentlyRead;
		}
		return read;
    }
    
    public boolean isAuthorizedFor(byte appletKey){
	   return (mConnectionState != null && mConnectionState.length == 1 && mConnectionState[0] == CONNECTION_ESTABLISHED);
   }
   
   
    public Shareable getShareableInterfaceObject(AID client_aid, byte parameter) {
	AID prevAID = JCSystem.getPreviousContextAID();
	if (!(client_aid.equals(prevAID))){
		return (this);
		
	}else{
		return null;
	}
    }
}
