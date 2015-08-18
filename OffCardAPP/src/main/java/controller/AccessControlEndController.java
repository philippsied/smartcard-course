package controller;

import java.util.function.Consumer;

import javax.smartcardio.CardException;

import clientAPI.ClientFactory;
import clientAPI.TicketManager;
import clientAPI.data.Ticket;
import connection.TerminalConnection;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AccessControlEndController {

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

    @FXML
    protected void handleEndAction(ActionEvent event) {
	final String styleBkp = accessLamp.getStyle();
	final String insertBkp = insertCard.getText();
	final String noteBkp = accessNote.getText();
	try {
	    TicketManager ticketManager = ClientFactory.getTicketManager(TerminalConnection.INSTANCE.getCurrentCard());

	    Ticket currentTicket = ticketManager.getTicket();
	} catch (CardException e) {
	    System.err.println("AccessControlEndController: " + e.getLocalizedMessage());
	    accessNote.setText(MSG_ON_ACCESS_UNKNOWN);
	}
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

}
