package controller.server;

import java.util.ArrayList;
import java.util.List;

import controller.data.TicketEntry;
import controller.data.TicketEntry.DurationUnit;

public class TicketServer {

    private static final int costPerMinuteInCent = 15;
    private static final int fixedCost = 700;
    private static final int thresholdForFixedCost = 45;

    private static final int bonusCreditsPerMinute = 2;
    private static final int fixedBonusCredit = 200;
    private static final int thresholdForFixedBonusCredit = 90;

    public static List<TicketEntry> getAvailableTickets() {
	ArrayList<TicketEntry> ticketList = new ArrayList<TicketEntry>();
	ticketList.add(new TicketEntry((byte) 20, DurationUnit.MINUTE, "Kurzstrecke", (short) 180, (short) 270));
	ticketList.add(new TicketEntry((byte) 60, DurationUnit.MINUTE, "Einzelfahrt", (short) 250, (short) 375));
	ticketList.add(new TicketEntry((byte) 20, DurationUnit.MINUTE, "Extrakarte", (short) 180, (short) 270));
	ticketList.add(new TicketEntry((byte) 1, DurationUnit.DAY, "Tageskarte", (short) 690, (short) 1035));
	ticketList.add(new TicketEntry((byte) 7, DurationUnit.DAY, "Wochenkarte", (short) 2370, (short) 3555));
	ticketList.add(new TicketEntry((byte) 30, DurationUnit.DAY, "Monatskarte", (short) 6900, (short) 10350));
	ticketList.add(new TicketEntry((byte) 10, DurationUnit.MINUTE, "Test", (short) 100, (short) 100));
	return ticketList;
    }

    public static int calculateCost(long startTS, long endTS) {
	return calculateAmountbyDiff(startTS, endTS, costPerMinuteInCent, thresholdForFixedCost, fixedCost);
    }

    public static int calculateBonusCredits(long startTS, long endTS) {
	return calculateAmountbyDiff(startTS, endTS, bonusCreditsPerMinute, thresholdForFixedBonusCredit,
		fixedBonusCredit);
    }

    private static int calculateAmountbyDiff(long startTS, long endTS, int amountPerMin, int thresholdForFixedRate,
	    int fixedRate) {
	long diffInMin = (endTS > startTS) ? endTS - startTS : startTS - endTS;
	diffInMin /= 60;
	if (diffInMin < thresholdForFixedRate) {
	    return ((int) diffInMin) * amountPerMin;
	} else {
	    return fixedRate;
	}
    }
}
