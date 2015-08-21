package connection;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import clientAPI.data.EncryptFunction;

public class LocalKeyStore {

    private RSAPublicKey mPublicKey = null;
    private RSAPrivateKey mPrivateKey = null;

    public LocalKeyStore(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
	mPublicKey = publicKey;
	mPrivateKey = privateKey;
    }

    public EncryptFunction getEncryptionFunc() {
	return (plaintext -> {
	    try {
		Cipher sendCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		sendCipher.init(Cipher.ENCRYPT_MODE, mPrivateKey);
		return sendCipher.doFinal(plaintext);
	    } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException
		    | InvalidKeyException e) {
		e.printStackTrace();
	    }
	    return null;
	});
    }

    public RSAPublicKey getPublicKey() {
	return mPublicKey;
    }
}
