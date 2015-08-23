package controller.server;

import java.util.ArrayList;
import java.util.List;

import controller.data.TicketEntry;
import controller.data.TicketEntry.DurationUnit;

/**
 * Klasse zur Simulation eines Ticketservers zur Abfrage von Preisen,
 * Bonuspunkten und Fahrkarten.
 * 
 * @author player
 *
 */
public class TicketServer {

    /**
     * Nachzahlungskosten pro Minute in Euro-Cent
     */
    private static final int costPerMinuteInCent = 15;

    /**
     * Höhe der Nachzahlungspauschale
     */
    private static final int fixedCost = 700;

    /**
     * Schwellwert für Nachzahlungspauschale
     */
    private static final int thresholdForFixedCost = 45;

    /**
     * Bonuspunkte je gefahrener Minute
     */
    private static final int bonusCreditsPerMinute = 2;

    /**
     * Höhe der Bonuspunkte-Pauschale
     */
    private static final int fixedBonusCredit = 200;

    /**
     * Schwellwert für Bonuspunkte-Pauschale
     */
    private static final int thresholdForFixedBonusCredit = 90;

    /**
     * Liefert die aktuell angebotenen Tickets einschließlich Preis,
     * Gültigkeitsdauer und Bezeichnung
     * 
     * @return Liste mit erwerbbaren Tickets
     */
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

    /**
     * Berechne die Nachzahlungskosten
     * 
     * @param startTS
     * @param endTS
     * @return Resultierende Nachzahlungskosten
     */
    public static int calculateCost(long startTS, long endTS) {
	return calculateAmountbyDiff(startTS, endTS, costPerMinuteInCent, thresholdForFixedCost, fixedCost);
    }

    /**
     * Berechne die Bonuspunkte
     * 
     * @param startTS
     * @param endTS
     * @return Resultierende Bonuspunkte
     * 
     */
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
