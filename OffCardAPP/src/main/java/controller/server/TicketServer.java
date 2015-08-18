package controller.server;

import java.util.ArrayList;
import java.util.List;

import controller.data.TicketEntry;
import controller.data.TicketEntry.DurationUnit;

public class TicketServer {

    public static List<TicketEntry> getAvailableTickets(){
	ArrayList<TicketEntry> ticketList = new ArrayList<TicketEntry>();
	ticketList.add(new TicketEntry((byte) 20, DurationUnit.MINUTE, "Kurzstrecke", (short) 180, (short) 270));
	ticketList.add(new TicketEntry((byte) 60, DurationUnit.MINUTE, "Einzelfahrt", (short) 250, (short) 375));
	ticketList.add(new TicketEntry((byte) 20, DurationUnit.MINUTE, "Extrakarte", (short) 180, (short) 270));
	ticketList.add(new TicketEntry((byte) 1, DurationUnit.DAY, "Tageskarte", (short) 690, (short) 1035));
	ticketList.add(new TicketEntry((byte) 7, DurationUnit.DAY, "Wochenkarte", (short) 2370, (short) 3555));
	ticketList.add(new TicketEntry((byte) 30, DurationUnit.DAY, "Monatskarte", (short) 6900, (short) 10350));
	
	return ticketList;
    }
}
