package clientAPI.data;

import static com.google.common.base.Preconditions.checkArgument;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import clientAPI.impl.OncardAPI.TicketManagerOncard;

public class Ticket {
    private final int mStartValidityTS;
    private final int mExpireValidityTS;
    private final String mDescription;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 
     * @param startValidityTS
     *            Unix-Zeitstempel für den Gültigkeitsbeginn des Tickets.
     *            Sekunden werden aus Platzgründen abgeschnitten.
     * @param expireValidityTS
     *            Unix-Zeitstempel für das Gültigkeitsende des Tickets. Sekunden
     *            werden aus Platzgründen abgeschnitten.
     * @param description
     *            Kurzbeschreibung des Tickets
     */
    public Ticket(long startValidityTS, long expireValidityTS, String description) {
	// Zeitstempel in minutengenaue Speicherung überführen
	this((int) (startValidityTS / 60), (int) (expireValidityTS / 60), description.getBytes(DEFAULT_CHARSET));
    }

    private Ticket(int startValidityTSinMin, int expireValidityTSinMin, byte[] description) {
	checkArgument(description.length <= TicketManagerOncard.TICKET_DESCRIPTION_MAX_LENGTH,
		"Description too long. Max. " + TicketManagerOncard.TICKET_DESCRIPTION_MAX_LENGTH + " bytes (UTF-8)");
	mDescription = new String(description).trim();
	mStartValidityTS = startValidityTSinMin;
	mExpireValidityTS = expireValidityTSinMin;

    }

    public long getStartValidityTS() {
	return mStartValidityTS * 60;
    }

    public long getExpireValidityTS() {
	return mExpireValidityTS * 60;
    }

    public String getDescription() {
	return mDescription;
    }

    public byte[] toByteArray() {
	ByteBuffer bb = ByteBuffer.allocate(TicketManagerOncard.TICKET_MAX_SIZE);
	bb.putShort((short) ((mStartValidityTS & 0xFFFF0000) >> 16));
	bb.putShort((short) (mStartValidityTS & 0xFFFF));
	bb.putShort((short) ((mExpireValidityTS & 0xFFFF0000) >> 16));
	bb.putShort((short) (mExpireValidityTS & 0xFFFF));
	bb.put(mDescription.getBytes(DEFAULT_CHARSET));
	while (bb.hasRemaining()) {
	    bb.put((byte) ' ');
	}
	return bb.array();
    }

    public static Ticket extractTicket(byte[] data) {
	if (TicketManagerOncard.TICKET_MIN_SIZE <= data.length) {
	    ByteBuffer bb = ByteBuffer.wrap(data);
	    short tmpStartTShigh = bb.getShort();
	    int tmpStartTS = (tmpStartTShigh << 16) | bb.getShort();

	    short tmpExpTShigh = bb.getShort();
	    int tmpExpTS = (tmpExpTShigh << 16) | bb.getShort();

	    byte[] tmpDescription = new byte[TicketManagerOncard.TICKET_DESCRIPTION_MAX_LENGTH];
	    bb.get(tmpDescription, 0, bb.remaining());

	    return new Ticket(tmpStartTS, tmpExpTS, tmpDescription);
	} else {
	    return null;
	}
    }
}
