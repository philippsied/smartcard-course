package clientAPI.data;

import static com.google.common.base.Preconditions.checkArgument;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import clientAPI.impl.OncardAPI.TicketManagerOncard;

/**
 * Klasse zur Repräsentation eines Tickets für den öffentlichen Verkehr.
 *
 */
public class Ticket {

    /**
     * Zeitstempel des Gültigkeitsbeginnes. In vergangene Minuten seit
     * Unix-Zeitbeginn.
     */
    private final int mStartValidityTS;

    /**
     * Zeitstempel des Gültigkeitsendes. In vergangene Minuten seit
     * Unix-Zeitbeginn.
     */
    private final int mExpireValidityTS;

    /**
     * Beschreibung des Tickets. Die maximale Größe wird durch
     * {@code TicketManagerOncard.TICKET_DESCRIPTION_MAX_LENGTH} limitiert.
     */
    private final String mDescription;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Erzeugt ein Ticket mit der angegebenen Gültigkeit und Beschreibung.
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

    /**
     * Liefert den an ganzen Minuten ausgerichteten Gültigkeitsbeginn als
     * Unix-Zeitstempel.
     * 
     * @return Gültigkeitsbeginn
     */
    public long getStartValidityTS() {
	return mStartValidityTS * 60;
    }

    /**
     * Liefert das an ganzen Minuten ausgerichteten Gültigkeitsende als
     * Unix-Zeitstempel.
     * 
     * @return Gültigkeitsende
     */
    public long getExpireValidityTS() {
	return mExpireValidityTS * 60;
    }

    /**
     * Liefert die Beschreibung des Tickets.
     * 
     * @return Beschreibung
     */
    public String getDescription() {
	return mDescription;
    }

    /**
     * Konvertiert das Ticket in seine Darstellung als Byte-Array. Das Ticket
     * wird dabei nicht verändert.
     * 
     * @return Darstellung des Tickets als Byte
     */
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

    /**
     * Erzeugt ein Ticket aus dem übergebenen Byte-Array.
     * 
     * @param data
     *            Ticket in Byte-Repräsentation
     * @return Eingelesenes Ticket
     */
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
