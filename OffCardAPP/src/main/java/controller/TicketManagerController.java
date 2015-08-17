package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.smartcardio.CardException;

import clientAPI.BonusCreditStore;
import clientAPI.ClientFactory;
import clientAPI.TicketManager;
import clientAPI.Wallet;
import clientAPI.data.Ticket;
import clientAPI.data.Ticket.DurationUnit;
import connection.TerminalConnection;
import controller.data.TicketEntry;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class TicketManagerController implements Initializable {

    private TicketManager tmanager = null;
    private Wallet wallet = null;
    private BonusCreditStore bonus = null;

    @FXML
    private ComboBox<TicketEntry> ticketCombo;

    @FXML
    private TextField amountField;

    @FXML
    protected void handleComboAction() {
	TicketEntry entry = (TicketEntry) ticketCombo.getSelectionModel().getSelectedItem();
	amountField.setText(entry.getEurString() + " oder " + entry.getmPoints() + "Punkte");
    }

    @FXML
    protected void handleMoneyAction() {

	try {
	    if (TerminalConnection.INSTANCE.connect()) {
		if (wallet == null) {
		    wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
		}

		if (tmanager == null) {
		    tmanager = ClientFactory.getTicketManager(TerminalConnection.INSTANCE.getCurrentCard());
		}
		
		TicketEntry tentry = ticketCombo.getSelectionModel().getSelectedItem();
		short balance = wallet.checkBalance();
		if (balance >= tentry.getCent()) {
		    wallet.removeMoney(tentry.getCent());
		    
		    short newBalance = wallet.checkBalance();
		    if((newBalance + tentry.getCent()) == balance){
			tmanager.setTicket(tentry.createTicket());
			amountField.setText("OK!");
		    }
		}else {
		    amountField.setText("Bitte Geldkarte aufladen!");
		}
		
	
	    } else {
		System.out.println("TicketManager: No card present");
	    }
	} catch (CardException e) {
	    amountField.setText("ERROR!");
	}

    }

    @FXML
    protected void handlePointAction() {

	try {
	    if (TerminalConnection.INSTANCE.connect()) {
		if (bonus == null) {
		    bonus = ClientFactory.getBonusCreditStore(TerminalConnection.INSTANCE.getCurrentCard());
		}

		if (tmanager == null) {
		    tmanager = ClientFactory.getTicketManager(TerminalConnection.INSTANCE.getCurrentCard());
		}
		
		TicketEntry tentry = ticketCombo.getSelectionModel().getSelectedItem();
		short balance = bonus.checkBalance();
		if (balance >= tentry.getmPoints()) {
		    bonus.removeBonusCredits(tentry.getmPoints());
		    
		    short newBalance = bonus.checkBalance();
		    if((newBalance + tentry.getmPoints()) == balance){
			tmanager.setTicket(tentry.createTicket());
			amountField.setText("OK!");
		    }
		    
		}else {
		    amountField.setText("Nicht genügend Punkte!");
		}
		
	
	    } else {
		System.out.println("TicketManager: No card present");
	    }
	} catch (CardException e) {
	    amountField.setText("ERROR!");
	}
	
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

	ticketCombo.getItems().add(new TicketEntry((byte) 20, DurationUnit.MINUTE, "Kurzstrecke1", (short) 180, (short) 270));
	ticketCombo.getItems().add(new TicketEntry((byte) 60, DurationUnit.MINUTE, "Einzelfahrt1", (short) 250, (short) 375));
	ticketCombo.getItems().add(new TicketEntry((byte) 20, DurationUnit.MINUTE, "Extrakarte", (short) 180, (short) 270));
	ticketCombo.getItems().add(new TicketEntry((byte) 1, DurationUnit.DAY, "Tageskarte", (short) 690, (short) 1035));
	ticketCombo.getItems().add(new TicketEntry((byte) 7, DurationUnit.DAY, "Wochenkarte1", (short) 2370, (short) 3555));
	ticketCombo.getItems().add(new TicketEntry((byte) 30, DurationUnit.DAY, "Monatskarte1", (short) 6900, (short) 10350));
    }

}
