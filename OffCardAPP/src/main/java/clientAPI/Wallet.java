package clientAPI;

import javax.smartcardio.CardException;

/**
 * Schnittstelle zur Festlegung der Funktionen des Geldbörsen-Applets.
 *
 */
public interface Wallet {

    /**
     * Fügt den angegebenen Geldbetrag in Cent der Geldbörse hinzu.
     * 
     * @param amountInCent
     *            Geldbetrag.
     * @throws CardException
     */
    public void addMoney(int amountInCent) throws CardException;

    /**
     * Zieht den angegebenen Geldbetrag in Cent von der Geldbörse ab.
     * 
     * @param amountInCent
     *            Geldbetrag.
     * @throws CardException
     */
    public void removeMoney(int amountInCent) throws CardException;

    /**
     * Liefert den aktuellen Kontostand
     * 
     * @return Kontostand
     */
    public int checkBalance() throws CardException;
}
