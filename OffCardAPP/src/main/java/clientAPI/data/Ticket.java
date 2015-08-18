package clientAPI.data;

import static com.google.common.base.Preconditions.checkArgument;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import clientAPI.impl.OncardAPI.TicketManagerOncard;

public class Ticket {
    private final short mStartValidityTS;
    private final short mExpireValidityTS;
    private final String mDescription;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 
     * @param startValidityTS
     *            Unix-Zeitstempel für den Gültigkeitsbeginn des Tickets.
     *            Millisekunden und Sekunden werden aus Platzgründen
     *            abgeschnitten.
     * @param expireValidityTS
     *            Unix-Zeitstempel für das Gültigkeitsende des Tickets.
     *            Millisekunden und Sekunden werden aus Platzgründen
     *            abgeschnitten.
     * @param description
     *            Kurzbeschreibung des Tickets
     */
    public Ticket(long startValidityTS, long expireValidityTS, String description) {
	// Zeitstempel in minutengenaue Speicherung überführen
	this((short) (startValidityTS / (60 * 1000)), (short) (expireValidityTS / (60 * 1000)),
		description.getBytes(DEFAULT_CHARSET));
    }

    public Ticket(short startValidityTS, short expireValidityTS, byte[] description) {
	checkArgument(description.length <= TicketManagerOncard.TICKET_DESCRIPTION_LENGTH,
		"Description too long. Max. " + TicketManagerOncard.TICKET_DESCRIPTION_LENGTH + " bytes (UTF-8)");
	mDescription = new String(description).trim();
	mStartValidityTS = startValidityTS;
	mExpireValidityTS = expireValidityTS;

    }

    public long getStartValidityTS() {
	return mStartValidityTS * 60 * 1000;
    }

    public long getExpireValidityTS() {
	return mExpireValidityTS * 60 * 1000;
    }

    public String getDescription() {
	return mDescription;
    }

    public byte[] toByteArray() {
	ByteBuffer bb = ByteBuffer.allocate(TicketManagerOncard.TICKET_SIZE);
	bb.putShort(mStartValidityTS);
	bb.putShort(mExpireValidityTS);
	bb.put(mDescription.getBytes(DEFAULT_CHARSET));
	while (bb.hasRemaining()) {
	    bb.put((byte) ' ');
	}
	return bb.array();
    }
}
