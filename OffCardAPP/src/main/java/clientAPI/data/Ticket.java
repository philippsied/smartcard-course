package clientAPI.data;

import static com.google.common.base.Preconditions.checkArgument;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import clientAPI.impl.OncardAPI.TicketManagerOncard;

public class Ticket {
    private final short mStartValidityTS;
    private final byte mDuration;
    private final DurationUnit mDurationUnit;
    private final String mDescription;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 
     * @param startValidityTS
     *            Unix-Zeitstempel für den Gültigkeitsbeginn des Tickets.
     *            Millisekunden und Sekunden werden aus Platzgründen
     *            abgeschnitten.
     * @param duration
     *            Einheitelose Gültigkeitsdauer
     * @param durationUnit
     *            Einheit der Gültigkeitsdauer
     * @param description
     *            Kurzbeschreibung des Tickets
     */
    public Ticket(long startValidityTS, byte duration, DurationUnit durationUnit, String description) {
	// Zeitstempel in minutengenaue Speicherung überführen
	this((short) (startValidityTS / (60 * 1000)), duration, durationUnit, description.getBytes(DEFAULT_CHARSET));
    }

    public Ticket(short startValidityTS, byte duration, DurationUnit durationUnit, byte[] description) {
	checkArgument(description.length <= TicketManagerOncard.TICKET_DESCRIPTION_LENGTH,
		"Description too long. Max. " + TicketManagerOncard.TICKET_DESCRIPTION_LENGTH + " bytes (UTF-8)");
	mDescription = new String(description).trim();
	mStartValidityTS = startValidityTS;
	mDuration = duration;
	mDurationUnit = durationUnit;

    }

    public long getStartValidityTS() {
	return mStartValidityTS * 60 * 1000;
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

    public byte[] toByteArray() {
	ByteBuffer bb = ByteBuffer.allocate(TicketManagerOncard.TICKET_SIZE);
	bb.putShort(mStartValidityTS);
	bb.put(mDuration);
	bb.put((byte) (mDurationUnit.ordinal() & 0xff));
	bb.put(mDescription.getBytes(DEFAULT_CHARSET));
	while (bb.hasRemaining()) {
	    bb.putChar(' ');
	}
	return bb.array();
    }

    /**
     * Aufzählungsdatentyp für Einheit der Gültigkeitsdauer von Fahrkarten
     *
     */
    public enum DurationUnit {
	MINUTE, HOUR, DAY, MONTH;

	public static DurationUnit getByOrdinal(byte ordinal) {
	    return values()[ordinal];
	}
    }
}
