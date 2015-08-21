package clientAPI.data;

public interface EncryptFunction {
    /**
     * Verschlüsselt den übergebenen plaintext. Die Instanz einer
     * implementierenden Klasse stellt den geheimen Schlüssel bereit.
     * 
     * @param plaintext
     * @return Ciphertext
     */
    public byte[] encryptWithIntegratedKey(byte[] plaintext);
}
