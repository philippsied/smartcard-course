package clientAPI;

import java.security.interfaces.RSAPublicKey;

import javax.smartcardio.CardException;

import clientAPI.data.EncryptFunction;

/**
 * Schnittstelle zur Festlegung der Funktionalität des Crypto-Applet.
 * 
 *
 */
public interface CryptoMgr {

    /**
     * Authentifiziert die aktuell bestehende Verbindung zur Smartcard.
     * 
     * @param encrypt
     *            Funktion zur Verschlüsselung von Daten. Die Smartcard muss dem
     *            zugehörigen Schlüssel vertrauen.
     * @return True, wenn die Authentifizierung erfolgreich war
     * @throws CardException
     */
    public boolean authenticateConnection(EncryptFunction encrypt) throws CardException;

    /**
     * Initialisiert die Smartcard mit der angebenen Karten-ID sowie dem
     * öffentlichen Schlüssel, der als Vertrauensanker in der Karte gespeichert
     * wird.
     * <p>
     * Dieser Schritt kann nur einmal im Lebenszyklus einer Karte durchgeführt
     * werden.
     * 
     * @param cardId
     *            Karte-ID
     * @param offcardPubKey
     *            öffentlicher RSA-Schlüssel (2048 Bit)
     * @throws CardException
     */
    public void initializeCard(int cardId, RSAPublicKey offcardPubKey) throws CardException;

    /**
     * Liefert die gespeichert Karten-ID.
     * 
     * @return Karten-ID
     * @throws CardException
     */
    public int getCardId() throws CardException;

    /**
     * Liefert den gespeicherten öffentlichen RSA-Schlüssel.
     * 
     * @return Öffentlicher RSA-Schlüssel (2048 Bit)
     * @throws CardException
     */
    public RSAPublicKey getStoredPublicKey() throws CardException;

}
