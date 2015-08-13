package connection;

import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 * Singleton-Klasse zur Bereitstellung des Zugriffes auf das aktuelle
 * Kartenlesegerät.
 *
 */
public enum TerminalConnection {
	/**
	 * Singleton-Instanz der Klasse
	 */
	INSTANCE;

	/**
	 * Aktuell verwendetes Kartenlesegerät
	 */
	private CardTerminal terminal = null;

	/**
	 * Aktuell eingelegte Smartcard
	 */
	private Card currentCard = null;

	/**
	 * Flag zur Speicherung, ob Verbindung zu einer Karte besteht
	 */
	private boolean isConnected = false;

	public static List<CardTerminal> getTerminals() throws CardException {
		final TerminalFactory terminalFactory = TerminalFactory.getDefault();
		return terminalFactory.terminals().list();
	}

	/**
	 * Wählt das übergebene Terminal aus. Sofern auf dem alten Terminal eine
	 * Verbindung zu einer Smartcard besteht wird diese zunächst getrennt.
	 * 
	 * @param newTerminal
	 *            das zu setzende Terminal
	 * @throws CardException
	 *             Fehler die während der evtl. notwendigen Trennung der
	 *             Smartcard auftreten können.
	 */
	public synchronized void chooseTerminal(CardTerminal newTerminal) throws CardException {
		if (isConnected) {
			disconnect();
		}
		terminal = newTerminal;
		isConnected = false;
		currentCard = null;
	}

	public synchronized boolean connect() throws CardException {
		try {
			currentCard = terminal.connect("*");
			isConnected = true;
			return true;
		} catch (NullPointerException e) {
			throw new CardException("Kein Lesegerät ausgewählt");
		}
	}

	/**
	 * Trennt die Verbindung zur aktuellen Smartcard
	 * 
	 * @return True, wenn erfolgreich
	 * @throws CardException
	 *             Fehler die während der Trennung der Smartcard auftreten
	 *             können.
	 */
	public synchronized boolean disconnect() throws CardException {
		try {
			currentCard.disconnect(true);
			currentCard = null;
			isConnected = false;
			return true;
		} catch (NullPointerException e) {
			throw new CardException("Keine offene Verbindung zu einer Karte");
		}
	}

	/**
	 * Liefert das aktuelle Kartenlesegerät
	 * 
	 * @return Aktuelles Kartenlesegerät
	 */
	public CardTerminal getTerminal() {
		return terminal;
	}

	/**
	 * Liefert den aktuellen Verbindungsstatus zum Terminal
	 * 
	 * @return True, wenn verbunden
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Liefert die aktuell mit dem Terminal verbunde Smartcard.
	 * 
	 * @return Smartcard oder {@code null}
	 */
	public Card getCurrentCard() {
		return currentCard;
	}
}
