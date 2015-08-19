package clientAPI;

import javax.smartcardio.CardException;

public interface BonusCreditStore {

	/**
	 *
	 */
	public void addBonusCredits(int amount) throws CardException;

	/**
	 *
	 */
	public void removeBonusCredits(int amount) throws CardException;

	/**
	 *
	 *
	 */
	public int checkBalance() throws CardException;
}
