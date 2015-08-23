package clientAPI;

import javax.smartcardio.CardException;

/**
 * Schnittstelle zur Festlegung der Funktionalität des Bonussystem-Applets.
 * 
 *
 */
public interface BonusCreditStore {

    /**
     * Fügt die angegebenen Anzahl an Bonuspunkten dem Bonuspunktekonto hinzu.
     * 
     * @param amount
     *            Anzahl Bonuspunkte
     * @throws CardException
     */
    public void addBonusCredits(int amount) throws CardException;

    /**
     * Entfernt die angegebene Anzahl an Bonuspunkten von der Karte.
     * 
     * @param amount
     *            Anzahl Bonuspunkte
     * @throws CardException
     */
    public void removeBonusCredits(int amount) throws CardException;

    /**
     * Liefert den aktuellen Bonuspunktestand.
     * 
     * @return Bonuspunktestand
     * @throws CardException
     */
    public int checkBalance() throws CardException;
}
