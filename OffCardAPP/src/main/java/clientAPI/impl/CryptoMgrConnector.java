package clientAPI.impl;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.ResponseAPDU;

import org.apache.commons.lang3.ArrayUtils;

import clientAPI.CryptoMgr;
import clientAPI.data.EncryptFunction;
import clientAPI.impl.OncardAPI.CryptoMgrOncard;

public class CryptoMgrConnector extends GenericConnector implements CryptoMgr {

    public CryptoMgrConnector(Card card) {
	super(CryptoMgrOncard.AID, card);

    }

    @Override
    public boolean authenticateConnection(EncryptFunction encryptWithPrivateKey) throws CardException {
	ResponseAPDU resStart = genericCommand(CryptoMgrOncard.INS_START_CHALLENGE_RESPONSE, null);
	byte[] challenge = resStart.getData();
	if (challenge.length != CryptoMgrOncard.CHALLENGE_SIZE) {
	    return false;
	}
	ResponseAPDU resFinish = genericCommand(CryptoMgrOncard.INS_FINISH_CHALLENGE_RESPONSE,
		encryptWithPrivateKey.encryptWithIntegratedKey(challenge));
	return true;
    }

    @Override
    public void initializeCard(int cardId, RSAPublicKey offcardPubKey) throws CardException {
	ByteBuffer bb = ByteBuffer.allocate(2 * (CryptoMgrOncard.RSA_KEY_SIZE_IN_BITS / 8) + Integer.BYTES);
	bb.putShort((short) ((cardId & 0xFFFF0000) >> 16));
	bb.putShort((short) (cardId & 0xFFFF));
	byte[] modulus = convertToByteArray(offcardPubKey.getModulus());
	byte[] pubExponent = convertToByteArray(offcardPubKey.getPublicExponent());
	bb.putShort((short) modulus.length);
	bb.putShort((short) pubExponent.length);
	bb.put(modulus);
	bb.put(pubExponent);
	ResponseAPDU response = genericCommand(CryptoMgrOncard.INS_INITIALIZE,
		ArrayUtils.subarray(bb.array(), 0, bb.position()));
    }

    @Override
    public int getCardId() throws CardException {
	ResponseAPDU response = genericCommand(CryptoMgrOncard.INS_GET_CARD_ID, null);
	ByteBuffer bb = ByteBuffer.wrap(response.getData());
	short tmpIdhigh = bb.getShort();
	return (tmpIdhigh << 16) | bb.getShort();
    }

    @Override
    public RSAPublicKey getStoredPublicKey() throws CardException {
	ResponseAPDU response = genericCommand(CryptoMgrOncard.INS_GET_TRUSTED_PUBKEY, null);
	ByteBuffer bb = ByteBuffer.wrap(response.getData());
	short modulusLength = bb.getShort();
	short publicExpLength = bb.getShort();
	int offset = bb.position();
	BigInteger modulus = convertToBigInteger(ArrayUtils.subarray(bb.array(), offset, offset + modulusLength));
	offset += modulusLength;
	BigInteger exponent = convertToBigInteger(ArrayUtils.subarray(bb.array(), offset, offset + publicExpLength));
	RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulus, exponent);
	try {
	    return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(publicSpec);
	} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    protected void checkForError(ResponseAPDU response) throws CardException {
	if (response.getSW() != 0x9000)
	    throw new CardException("Error: " + Integer.toHexString(response.getSW() & 0xffff));
    }

    private byte[] convertToByteArray(BigInteger bigInt) {
	byte[] tmp = bigInt.toByteArray();
	// Entfernt in einigen Fällen von BigInteger benötigte führende Null für
	// Vorzeichen
	return (tmp[0] == 0) ? ArrayUtils.subarray(tmp, 1, tmp.length) : tmp;
    }

    private BigInteger convertToBigInteger(byte[] bigEndianInt) {
	// Püft, ob MSB
	if ((bigEndianInt[0] & 0x80) == 0) {
	    return new BigInteger(bigEndianInt);
	} else {
	    return new BigInteger(ArrayUtils.add(bigEndianInt, 0, (byte) 0));
	}
    }
}
