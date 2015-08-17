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

@SuppressWarnings("restriction")
public class TicketManagerController implements Initializable {

    private TicketManager tmanager = null;
    private Wallet wallet = null;
    private BonusCreditStore bonus = null;

    @FXML
    private ComboBox<TicketEntry> ticketCombo;

    @FXML
    private TextField amountField;
    
    @FXML
    private TextField ticketField;
    
    @FXML
    private TextField curPointField;
    
    @FXML
    private TextField curMoneyField;

    @FXML
    protected void handleComboAction() {
	TicketEntry entry = (TicketEntry) ticketCombo.getSelectionModel().getSelectedItem();
	amountField.setText(entry.getEurString() + " oder " + entry.getmPoints() + "Punkte");
	
	try {
	    if (TerminalConnection.INSTANCE.connect()) {
		if (wallet == null) {
		    wallet = ClientFactory.getWallet(TerminalConnection.INSTANCE.getCurrentCard());
		}

		if (bonus == null) {
		    bonus = ClientFactory.getBonusCreditStore(TerminalConnection.INSTANCE.getCurrentCard());
		}

		short currentMoney = wallet.checkBalance();
		short currentPoints = bonus.checkBalance();
		
		short afterMoney = (short) (currentMoney - entry.getCent());
		if( afterMoney >= 0){
		    curMoneyField.setText(getEur(currentMoney) + " -> " + getEur(afterMoney));
		}else{
		    curMoneyField.setText(getEur(currentMoney));
		}
		    
		short afterPoints = (short) (currentPoints - entry.getmPoints());
		if( afterPoints >= 0){
		    curPointField.setText(currentPoints + "P -> " + afterPoints + "P");
		}else{
		    curPointField.setText(currentPoints + "P");
		}
				    

	
	    } else {
		System.out.println("TicketManager: No card present");
	    }
	} catch (CardException e) {
	    amountField.setText("ERROR!");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
	} catch (NullPointerException e){
	    amountField.setText("Kein Ticket gewählt!");
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
    
    
    @FXML
    protected void handleGetTicketAction() {
	try {
	    if (TerminalConnection.INSTANCE.connect()) {

		if (tmanager == null) {
		    tmanager = ClientFactory.getTicketManager(TerminalConnection.INSTANCE.getCurrentCard());
		}
		
		Ticket currentTicket = tmanager.getTicket();
		ticketField.setText(currentTicket.getDescription());
		
	    } else {
		System.out.println("TicketManager: No card present");
	    }
	} catch (CardException e) {
	    ticketField.setText("ERROR!");
	}
    }
    
    private String getEur(short value){
	return String.format("%1$,.2f€",(float) value/100);
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {

	ticketCombo.getItems().add(new TicketEntry((byte) 20, DurationUnit.MINUTE, "Kurzstrecke", (short) 180, (short) 270));
	ticketCombo.getItems().add(new TicketEntry((byte) 60, DurationUnit.MINUTE, "Einzelfahrt", (short) 250, (short) 375));
	ticketCombo.getItems().add(new TicketEntry((byte) 20, DurationUnit.MINUTE, "Extrakarte", (short) 180, (short) 270));
	ticketCombo.getItems().add(new TicketEntry((byte) 1, DurationUnit.DAY, "Tageskarte", (short) 690, (short) 1035));
	ticketCombo.getItems().add(new TicketEntry((byte) 7, DurationUnit.DAY, "Wochenkarte", (short) 2370, (short) 3555));
	ticketCombo.getItems().add(new TicketEntry((byte) 30, DurationUnit.DAY, "Monatskarte", (short) 6900, (short) 10350));
    }

}
