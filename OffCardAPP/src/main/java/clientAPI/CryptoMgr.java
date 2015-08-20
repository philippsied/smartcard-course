package clientAPI;

import java.security.interfaces.RSAPrivateKey;

public interface CryptoMgr {
	/**
	 * Before a couple of specific commands can be send to smartcard, the client
	 * have to authenticate on the smartcard.
	 * 
	 * @param key
	 *            The private key of a legitimate client. The corresponding
	 *            public key have to be stored on the smartcard.
	 * @return True, if the authentication was successful
	 */
	public boolean authenticate(RSAPrivateKey key);
}
