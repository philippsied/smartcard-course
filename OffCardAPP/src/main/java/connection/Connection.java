package connection;

import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.TerminalFactory;

public class Connection {
	
	private static final byte[] EMPTY = { (byte) 0x00, 0x00, 0x00, 0x00 };

	private static CardTerminal terminal;
	private static Card card;
	private static boolean isConnected = false;

	public static List<CardTerminal> getTerminals() {
		try {
			TerminalFactory terminalFactory = TerminalFactory.getDefault();
			return terminalFactory.terminals().list();
		} catch (CardException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}

	public static boolean connect() {

		try {
			card = terminal.connect("*");
			isConnected = true;
			return true;
		} catch (CardException e) {
			System.err.println("Bitte erst Karte einlegen");
		} catch (NullPointerException e) {
			System.err.println("Kein Lesegerät ausgewählt");
		}
		return false;
	}

	public static boolean disconnect() {

		try {
			card.disconnect(true);
			isConnected = false;
			return true;
		} catch (CardException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("Keine offene Verbindung zu einer Karte");
		}

		return false;
	}

	public static void setTerminal(CardTerminal terminal) {
		Connection.terminal = terminal;
	}

	public static CardChannel getCardChannel() {
		
		try {
			return card.getBasicChannel();
		} catch (NullPointerException e) {
			System.err.println("Nicht zur Karte verbunden");
		}
		
		return null;
	}

	public static boolean isConnected() {
		return isConnected;
	}
	
	public static ResponseAPDU send(CommandAPDU select) {

		ResponseAPDU answer = new ResponseAPDU(EMPTY);
		
		try {
			answer = getCardChannel().transmit(select);
		} catch (CardException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			System.err.println("Nicht zur Karte verbunden");
		} catch (NullPointerException e) {
			System.err.println("Keine Antwort erhalten");
		}

		return answer;
	}

}
