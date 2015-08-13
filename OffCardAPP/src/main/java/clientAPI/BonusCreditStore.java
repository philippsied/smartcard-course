package clientAPI;

import javax.smartcardio.CardException;

public interface BonusCreditStore {

	/**
	 *
	 */
	public void addBonusCredits(short amount) throws CardException;

	/**
	 *
	 */
	public void removeBonusCredits(short amount) throws CardException;

	/**
	 *
	 *
	 */
	public short checkBalance() throws CardException;
}
