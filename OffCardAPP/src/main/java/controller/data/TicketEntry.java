package controller.data;

import java.time.temporal.ChronoUnit;

public class TicketEntry {
    private final byte mDuration;
    private final DurationUnit mDurationUnit;
    private final String mDescription;
    private final short mPriceInCent;
    private final short mPriceInCredits;

    public TicketEntry(byte duration, DurationUnit durationUnit, String description, short priceInCent,
	    short priceInCredits) {
	mDuration = duration;
	mDurationUnit = durationUnit;
	mDescription = description;
	mPriceInCent = priceInCent;
	mPriceInCredits = priceInCredits;
    }

    public byte getDuration() {
	return mDuration;
    }

    public DurationUnit getDurationUnit() {
	return mDurationUnit;
    }

    public String getDescription() {
	return mDescription;
    }

    public short getPriceInCent() {
	return mPriceInCent;
    }

    public short getPriceInCredits() {
	return mPriceInCredits;
    }

    public String getEURString() {
	return String.format("%1$,.2f€", (float) mPriceInCent / 100);
    }

    @Override
    public String toString() {
	// return mDescription + String.format(" (%1$,.2f€)", mAmount);
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
