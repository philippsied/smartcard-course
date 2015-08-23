package controller;

import java.util.function.Consumer;

import javax.smartcardio.CardException;

import clientAPI.BonusCreditStore;
import clientAPI.ClientFactory;
import clientAPI.TicketManager;
import clientAPI.Wallet;
import clientAPI.data.Ticket;
import clientAPI.data.Trip;
import connection.TerminalConnection;
import controller.server.TicketServer;
import controller.server.TimeServer;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * 
 * Controller für die Zugangskontrolle - Ausstieg
 *
 */
public class AccessControlEndController {

    public static final String DEPARTURE = "Hauptbahnhof";

    public static final String MSG_ON_ACCESS_GRANT = "Bitte passieren Sie die Schranke.";
    public static final String MSG_ON_ACCESS_EXPIRE_TICKET_OK = "Gültigkeit des Tickets abgelaufen. Ausgleich über Kartenguthaben erfolgt.";
    public static final String MSG_ON_ACCESS_EXPIRE_TICKET_NO = "Gültigkeit des Tickets abgelaufen. Guthaben nicht ausreichend. Laden Sie bitte mind. folgenden Betrag auf ihre Karte auf und versuchen Sie es erneut: ";
    public static final String MSG_ON_ACCESS_UNKNOWN = "Zutritt nicht möglich. Bitte wenden Sie sich an unser Personal.";

    public static final long DELAY = 5000;

    @FXML
    private Label accessNote;
    @FXML
    private Label accessLamp;
    @FXML
    private Button insertCard;

    /**
     * Simulation für den Ausstieg in die Bahn ausführen - Prüft auf ein
     * gültiges Ticket und berechnet die Bonuspunkte für zufrühes Beenden der
     * Fahrt
     * 
     * @param event
     *            - Maus-Event
     */
    @FXML
    protected void handleEndAction(ActionEvent event) {
	final String styleBkp = accessLamp.getStyle();
	final String insertBkp = insertCard.getText();
	final String noteBkp = accessNote.getText();
	try {
	    if (checkAndGrantAccess()) {
		accessLamp
			.setStyle(styleBkp.replace("-fx-background-color: #FF0000;", "-fx-background-color: #00FF00;"));
	    } else {
		accessLamp
			.setStyle(styleBkp.replace("-fx-background-color: #FF0000;", "-fx-background-color: #FFFF00;"));
	    }
	    insertCard.setDisable(true);
	} catch (CardException e) {
	    System.err.println("AccessControlEndController: " + e.getLocalizedMessage());
	    accessNote.setText(MSG_ON_ACCESS_UNKNOWN);
	}
	ControllerHelper.waitBeforeTask(DELAY, new Consumer<WorkerStateEvent>() {
	    @Override
	    public void accept(WorkerStateEvent t) {
		accessNote.setText(noteBkp);
		insertCard.setText(insertBkp);
		insertCard.setDisable(false);
		accessLamp.setStyle(styleBkp);
	    }
	});
    }

    /**
     * Prüft auf Gültigkeit des Tickets
     * 
     * @return Ob Ticket zulässig
     * @throws CardException
     */
    private boolean checkAndGrantAccess() throws CardException {
	TicketManager ticketManager = ClientFactory.getTicketManager(TerminalConnection.INSTANCE.getCurrentCard());
	Trip currentTrip = ticketManager.getTrip();
	Ticket currentTicket = ticketManager.getTicket();
	long now = TimeServer.getTimestamp();

	if (currentTrip == null || currentTicket == null) {
	    accessNote.setText(MSG_ON_ACCESS_UNKNOWN);
	    return false;
	}
	if (now <= currentTicket.getExpireValidityTS()) {
	    grantBonusCredit(TicketServer.calculateBonusCredits(currentTrip.getStartTS(), now));
	    ticketManager.finishTrip();
	    accessNote.setText(MSG_ON_ACCESS_GRANT);
	    return true;
	} else {
	    return initiateAdditionalPayment(TicketServer.calculateCost(currentTicket.getExpireValidityTS(), now));
	}
    }

    /**
     * Auf zusätzliches Bezahlen Prüfen - wenn Gültigkeitsdauer des Tickets
     * überschritten
     * 
     * @param debts
     *            fälliger Betrag
     * @return ob Bezahlung erfolgreich
     */
    private boolean initiateAdditionalPayment(int debts) {
	Wallet wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
	try {
	    int balance = wallet.checkBalance();
	    if (balance >= debts) {
		wallet.removeMoney(debts);
		accessNote.setText(MSG_ON_ACCESS_EXPIRE_TICKET_OK);
		return true;
	    } else {
		accessNote.setText(MSG_ON_ACCESS_EXPIRE_TICKET_NO + " " + (debts - balance));
	    }
	} catch (CardException e) {
	    System.err.println("AccessControlEndController: " + e.getLocalizedMessage());
	}
	return false;
    }

    /**
     * Hinzufügen der Bonuspunkte auf der Karte
     * 
     * @param bonusCredits
     *            die zugesprochenen Bonuspunkte
     * @return true, wenn Punkte auf Karte eingetragen
     */
    private boolean grantBonusCredit(int bonusCredits) {
	BonusCreditStore store = ClientFactory.getBonusCreditStore(TerminalConnection.INSTANCE.getCurrentCard());
	try {
	    store.addBonusCredits(bonusCredits);
	    return true;
	} catch (CardException e) {
	    System.err.println("AccessControlEndController: " + e.getLocalizedMessage());
	}
	return false;
    }
}
