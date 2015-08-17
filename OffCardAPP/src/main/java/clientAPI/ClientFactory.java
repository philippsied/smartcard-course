package clientAPI;

import clientAPI.impl.BonusCreditStoreConnector;
import clientAPI.impl.CryptoStoreConnector;
import clientAPI.impl.PersonalDataConnector;
import clientAPI.impl.TicketManagerConnector;
import clientAPI.impl.WalletConnector;
import javax.smartcardio.Card;


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
	public static CryptoStore getCryptoStore(Card card) {
		return new CryptoStoreConnector(card);
	}
}
