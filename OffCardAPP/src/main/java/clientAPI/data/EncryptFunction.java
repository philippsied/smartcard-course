package clientAPI.data;

/**
 * Diese Schnittstelle legt die Funktion zur Verschlüsselung eines Plaintext
 * fest. I.d.R. werden implementiernde Instanzen durch Eigentümer des geheimen
 * Schlüssels erzeugt. Dadurch soll eine Herausgabe des Schlüssel vermieden
 * werden.
 *
 */
public interface EncryptFunction {

    /**
     * Verschlüsselt den übergebenen Plaintext. Die Instanz einer
     * implementierenden Klasse stellt den geheimen Schlüssel bereit.
     * 
     * @param plaintext
     * @return Ciphertext
     */
    public byte[] encryptWithIntegratedKey(byte[] plaintext);
}
