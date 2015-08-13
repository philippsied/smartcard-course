package clientAPI;

import javax.smartcardio.CardException;

public interface Wallet {
	/**
	 *
	 */
	public void addMoney(short amountInCent) throws CardException;

	/**
	 *
	 */
	public void removeMoney(short amountInCent) throws CardException;

	/**
	 *
	 *@return the current balance
	 */
	public short checkBalance() throws CardException;
}
