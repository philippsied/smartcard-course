package controller.data;

import java.time.temporal.ChronoUnit;

public class TicketEntry {

    /**
     * Gültigkeitsdauer des Tickets
     */
    private final byte mDuration;

    /**
     * Zeiteinheit für Gültigkeitsdauer
     */
    private final DurationUnit mDurationUnit;

    /**
     * Beschreibung für das Ticket
     */
    private final String mDescription;

    /**
     * Preis für das Ticket in Cent
     */
    private final short mPriceInCent;

    /**
     * Preis für das Ticket in Punkten
     */
    private final short mPriceInCredits;

    /**
     * Konstruktor für Ticketerstellung
     * 
     * @param duration
     *            Gültigkeitsdauer
     * @param durationUnit
     *            Zeiteinheit
     * @param description
     *            Beschreibung
     * @param priceInCent
     *            Preis in Cent
     * @param priceInCredits
     *            Preis in Punkten
     */
    public TicketEntry(byte duration, DurationUnit durationUnit, String description, short priceInCent,
	    short priceInCredits) {
	mDuration = duration;
	mDurationUnit = durationUnit;
	mDescription = description;
	mPriceInCent = priceInCent;
	mPriceInCredits = priceInCredits;
    }

    /**
     * Zurückgeben der Ticketbeschreibung
     * 
     * @return die Ticketbeschreibung als Byte
     */
    public byte getDuration() {
	return mDuration;
    }

    /**
     * Gibt die Zeiteinheit zurück
     * 
     * @return
     */
    public DurationUnit getDurationUnit() {
	return mDurationUnit;
    }

    /**
     * Zurückgeben der Ticketbeschreibung
     * 
     * @return die Ticketbeschreibung als String
     */
    public String getDescription() {
	return mDescription;
    }

    /**
     * Preis für das Ticket in Cent
     * 
     * @return Cent als short
     */
    public short getPriceInCent() {
	return mPriceInCent;
    }

    /**
     * Preis für das Ticket in Punkten
     * 
     * @return Punkte als short
     */
    public short getPriceInCredits() {
	return mPriceInCredits;
    }

    /**
     * Umwandlung der Cent in Euro
     * 
     * @return Foramtierter Euro-Betrag als String
     */
    public String getEURString() {
	return String.format("%1$,.2f€", (float) mPriceInCent / 100);
    }

    @Override
    public String toString() {
	return mDescription;
    }

    /**
     * Aufzählungsdatentyp für Einheit der Gültigkeitsdauer von Fahrkarten
     *
     */
    public enum DurationUnit {
	MINUTE(ChronoUnit.MINUTES), HOUR(ChronoUnit.HOURS), DAY(ChronoUnit.DAYS), MONTH(ChronoUnit.MONTHS);

	private final ChronoUnit unit;

	private DurationUnit(ChronoUnit unit) {
	    this.unit = unit;
	}

	public ChronoUnit getChronoUnit() {
	    return this.unit;
	}
    }
}
