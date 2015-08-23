package clientAPI;

import javax.smartcardio.Card;

import clientAPI.impl.BonusCreditStoreConnector;
import clientAPI.impl.CryptoMgrConnector;
import clientAPI.impl.PersonalDataConnector;
import clientAPI.impl.TicketManagerConnector;
import clientAPI.impl.WalletConnector;

/**
 * Klasse zur Erzeugung der benötigten Objekt-Instanzen für den Zugriff auf die
 * Applet-Funktionen.
 * 
 *
 */
public class ClientFactory {

    /**
     * Erzeugt eine Instanz für den Zugriff auf das Ticket- und Fahrten-Applet.
     * 
     * @param card
     *            Instanz der verbundenen Smartcard
     * @return Ticket- und Fahrten-Applet
     */
    public static TicketManager getTicketManager(Card card) {
	return new TicketManagerConnector(card);
    }

    /**
     * Erzeugt eine Instanz für den Zugriff auf das Geldbörsen-Applet.
     * 
     * @param card
     *            Instanz der verbundenen Smartcard
     * @return Geldbörsen-Applet
     */
    public static Wallet getWallet(Card card) {
	return new WalletConnector(card);
    }

    /**
     * Erzeugt eine Instanz für den Zugriff auf das Bonuspunkte-Applet.
     * 
     * @param card
     *            Instanz der verbundenen Smartcard
     * @return Bonuspunkte-Applet
     */
    public static BonusCreditStore getBonusCreditStore(Card card) {
	return new BonusCreditStoreConnector(card);
    }

    /**
     * Erzeugt eine Instanz für den Zugriff auf das Personaldaten-Applet.
     * 
     * @param card
     *            Instanz der verbundenen Smartcard
     * @return Personaldaten-Applet
     */
    public static PersonalData getPersonalData(Card card) {
	return new PersonalDataConnector(card);
    }

    /**
     * Erzeugt eine Instanz für den Zugriff auf das Crypto-Applet.
     * 
     * @param card
     *            Instanz der verbundenen Smartcard
     * @return Crypto-Applet
     */
    public static CryptoMgr getCryptoMgr(Card card) {
	return new CryptoMgrConnector(card);
    }
}
