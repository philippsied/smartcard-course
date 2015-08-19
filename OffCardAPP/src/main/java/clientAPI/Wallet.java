package clientAPI;

import javax.smartcardio.CardException;

public interface Wallet {
	/**
	 *
	 */
	public void addMoney(int amountInCent) throws CardException;

	/**
	 *
	 */
	public void removeMoney(int amountInCent) throws CardException;

	/**
	 *
	 *@return the current balance
	 */
	public int checkBalance() throws CardException;
}
