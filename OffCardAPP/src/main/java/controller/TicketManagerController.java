package controller;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.smartcardio.CardException;

import clientAPI.BonusCreditStore;
import clientAPI.ClientFactory;
import clientAPI.TicketManager;
import clientAPI.Wallet;
import clientAPI.data.Ticket;
import connection.TerminalConnection;
import controller.data.TicketEntry;
import controller.server.TicketServer;
import controller.server.TimeServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

@SuppressWarnings("restriction")
public class TicketManagerController implements Initializable {

    @FXML
    private ComboBox<TicketEntry> ticketCombo;
    @FXML
    private TextField amountField;
    @FXML
    private TextField validityField;
    @FXML
    private TextField ticketField;
    @FXML
    private TextField curPointField;
    @FXML
    private TextField curMoneyField;

    @FXML
    protected void handleComboAction() {
	try {
	    Wallet wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
	    BonusCreditStore bonus = ClientFactory.getBonusCreditStore(TerminalConnection.INSTANCE.getCurrentCard());

	    TicketEntry entry = (TicketEntry) ticketCombo.getSelectionModel().getSelectedItem();
	    amountField.setText(entry.getEURString() + " oder " + entry.getPriceInCredits() + "Punkte");
	    validityField.setText(entry.getDuration() + " " + entry.getDurationUnit().getChronoUnit().toString());
	    short currentMoney = wallet.checkBalance();
	    short currentPoints = bonus.checkBalance();

	    short afterMoney = (short) (currentMoney - entry.getPriceInCent());
	    if (afterMoney >= 0) {
		curMoneyField.setText(getEUR(currentMoney) + " -> " + getEUR(afterMoney));
	    } else {
		curMoneyField.setText(getEUR(currentMoney));
	    }

	    short afterPoints = (short) (currentPoints - entry.getPriceInCredits());
	    if (afterPoints >= 0) {
		curPointField.setText(currentPoints + "P -> " + afterPoints + "P");
	    } else {
		curPointField.setText(currentPoints + "P");
	    }
	} catch (CardException e) {
	    System.err.println("TicketManagerController: " + e.getLocalizedMessage());
	    amountField.setText("ERROR!");
	    validityField.setText("");
	}
    }

    @FXML
    protected void handleMoneyAction() {
	try {
	    Wallet wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
	    TicketManager tmanager = ClientFactory.getTicketManager(TerminalConnection.INSTANCE.getCurrentCard());

	    TicketEntry tentry = ticketCombo.getSelectionModel().getSelectedItem();
	    short balance = wallet.checkBalance();
	    if (balance >= tentry.getPriceInCent()) {
		wallet.removeMoney(tentry.getPriceInCent());

		short newBalance = wallet.checkBalance();
		if ((newBalance + tentry.getPriceInCent()) == balance) {
		    tmanager.setTicket(createTicket(tentry));
		    amountField.setText("OK!");
		    validityField.setText("");
		}
	    } else {
		amountField.setText("Bitte Geldkarte aufladen!");
		validityField.setText("");
	    }
	    curMoneyField.setText("");
	    curPointField.setText("");

	} catch (CardException e) {
	    System.err.println("TicketManagerController: " + e.getLocalizedMessage());
	    amountField.setText("ERROR!");
	    validityField.setText("");
	} catch (NullPointerException e) {
	    amountField.setText("Kein Ticket gewählt!");
	    validityField.setText("");
	}
    }

    @FXML
    protected void handlePointAction() {
	try {
	    BonusCreditStore bonus = ClientFactory.getBonusCreditStore(TerminalConnection.INSTANCE.getCurrentCard());
	    TicketManager tmanager = ClientFactory.getTicketManager(TerminalConnection.INSTANCE.getCurrentCard());

	    TicketEntry tentry = ticketCombo.getSelectionModel().getSelectedItem();
	    short balance = bonus.checkBalance();
	    if (balance >= tentry.getPriceInCredits()) {
		bonus.removeBonusCredits(tentry.getPriceInCredits());

		short newBalance = bonus.checkBalance();
		if ((newBalance + tentry.getPriceInCredits()) == balance) {
		    tmanager.setTicket(createTicket(tentry));
		    amountField.setText("OK!");
		    validityField.setText("");
		}
	    } else {
		amountField.setText("Nicht genügend Punkte!");
		validityField.setText("");
	    }
	    curMoneyField.setText("");
	    curPointField.setText("");

	} catch (CardException e) {
	    System.err.println("TicketManagerController: " + e.getLocalizedMessage());
	    amountField.setText("ERROR!");
	    validityField.setText("");
	}
    }

    @FXML
    protected void handleGetTicketAction() {
	try {
	    TicketManager tmanager = ClientFactory.getTicketManager(TerminalConnection.INSTANCE.getCurrentCard());
	    Ticket currentTicket = tmanager.getTicket();
	    if (currentTicket != null) {
		String expireTime = Instant.ofEpochSecond(currentTicket.getExpireValidityTS())
			.atZone(ZoneOffset.ofHours(1)).format(DateTimeFormatter.RFC_1123_DATE_TIME);
		ticketField.setText(expireTime);
	    } else {
		ticketField.setText("Kein Ticket vorhanden");
	    }
	} catch (CardException e) {
	    System.err.println("TicketManagerController: " + e.getLocalizedMessage());
	    ticketField.setText("ERROR!");
	}
    }

    public Ticket createTicket(TicketEntry entry) {
	long ticketStart = TimeServer.getTimestamp();
	long ticketExpire = TimeServer.getTimestampIn(entry.getDuration(), entry.getDurationUnit().getChronoUnit());
	return new Ticket(ticketStart, ticketExpire, entry.getDescription());
    }

    private String getEUR(short value) {
	return String.format("%1$,.2f€", (float) value / 100);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	ticketCombo.getItems().addAll(TicketServer.getAvailableTickets());
    }
}
