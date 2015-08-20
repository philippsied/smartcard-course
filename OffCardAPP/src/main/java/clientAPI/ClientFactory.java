package clientAPI;

import javax.smartcardio.Card;

import clientAPI.impl.BonusCreditStoreConnector;
import clientAPI.impl.CryptoMgrConnector;
import clientAPI.impl.PersonalDataConnector;
import clientAPI.impl.TicketManagerConnector;
import clientAPI.impl.WalletConnector;

public class ClientFactory {

    /**
     * 
     * @return
     */
    public static TicketManager getTicketManager(Card card) {
	return new TicketManagerConnector(card);
    }

    /**
     * 
     * @return
     */
    public static Wallet getWallet(Card card) {
	return new WalletConnector(card);
    }

    /**
     * 
     * @return
     */
    public static BonusCreditStore getBonusCreditStore(Card card) {
	return new BonusCreditStoreConnector(card);
    }

    /**
     * 
     * @return
     */
    public static PersonalData getPersonalData(Card card) {
	return new PersonalDataConnector(card);
    }

    /**
     * 
     * @return
     */
    public static CryptoMgr getCryptoStore(Card card) {
	return new CryptoMgrConnector(card);
    }
}
