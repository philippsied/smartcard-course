package controller;

import java.util.function.Consumer;

import javax.smartcardio.CardException;

import clientAPI.ClientFactory;
import clientAPI.TicketManager;
import clientAPI.data.Ticket;
import clientAPI.data.Trip;
import connection.TerminalConnection;
import controller.server.TimeServer;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * 
 * Controller für die Zugangskontrolle - Einstieg
 *
 */
public class AccessControlStartController {

    public static final String DEPARTURE = "Hauptbahnhof";

    public static final String MSG_ON_ACCESS_GRANT = "Bitte passieren Sie die Schranke.";
    public static final String MSG_ON_ACCESS_DENY = "Zutritt verweigert. Ungültiges Ticket";
    public static final String MSG_ON_ACCESS_UNKNOWN = "Zutritt nicht möglich. Bitte wenden Sie sich an unser Personal.";

    public static final long DELAY = 5000;

    @FXML
    private Label accessNote;
    @FXML
    private Label accessLamp;
    @FXML
    private Button insertCard;

    /**
     * Simulation für den Einstieg in die Bahn ausführen - Prüft auf ein
     * gültiges Ticket - Eine Fahrt wird auf der Karte aktiviert
     * 
     * @param event
     *            - Maus-Event
     */
    @FXML
    protected void handleStartAction(ActionEvent event) {
	final String styleBkp = accessLamp.getStyle();
	final String insertBkp = insertCard.getText();
	final String noteBkp = accessNote.getText();
	try {
	    if (checkAndGrantAccess()) {
		accessLamp
			.setStyle(styleBkp.replace("-fx-background-color: #FF0000;", "-fx-background-color: #00FF00;"));
		accessNote.setText(MSG_ON_ACCESS_GRANT);
	    } else {
		accessLamp
			.setStyle(styleBkp.replace("-fx-background-color: #FF0000;", "-fx-background-color: #FFFF00;"));
		accessNote.setText(MSG_ON_ACCESS_DENY);
	    }
	} catch (CardException e) {
	    System.err.println("AccessControlStartController: " + e.getLocalizedMessage());
	    accessNote.setText(MSG_ON_ACCESS_UNKNOWN);
	}
	insertCard.setDisable(true);

	ControllerHelper.waitBeforeTask(DELAY, new Consumer<WorkerStateEvent>() {
	    @Override
	    public void accept(WorkerStateEvent t) {
		accessNote.setText(noteBkp);
		insertCard.setText(insertBkp);
		accessLamp.setStyle(styleBkp);
		insertCard.setDisable(false);
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
	Trip start = new Trip(TimeServer.getTimestamp(), DEPARTURE);
	Ticket currentTicket = ticketManager.getTicket();
	if (currentTicket == null)
	    return false;
	if (start.getStartTS() < currentTicket.getStartValidityTS())
	    return false;
	if (start.getStartTS() > currentTicket.getExpireValidityTS())
	    return false;
	ticketManager.startTrip(start);
	return true;
    }
}
