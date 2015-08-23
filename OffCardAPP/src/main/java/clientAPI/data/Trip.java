package clientAPI.data;

import static com.google.common.base.Preconditions.checkArgument;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import clientAPI.impl.OncardAPI.TicketManagerOncard;

/**
 * Klasse zur Repräsentation einer begonnenen Fahrt.
 * 
 * @author player
 *
 */
public class Trip {

    /**
     * Zeitstempel des Fahrtbeginnes. In vergangene Minuten seit
     * Unix-Zeitbeginn.
     */
    private final int mStartTS;

    /**
     * Bezeichnung des Abfahrtsbahnhof. Die maximale Größe wird durch
     * {@code TicketManagerOncard.TRIP_DEPARTURE_MAX_LENGTH} limitiert.
     */
    private final String mDeparture;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Erzeugt eine Fahrt aus der Bezeichnung des Abfahrtsbahnhof und dem
     * Fahrtbeginn.
     * 
     * @param startTS
     *            Unix-Zeitstempel für den Zeitpunkt des Fahrtbeginnes. Sekunden
     *            werden aus Platzgründen abgeschnitten.
     * @param departure
     *            Abfahrtsbahnhof
     */
    public Trip(long startTS, String departure) {
	// Zeitstempel in minutengenaue Speicherung überführen
	this((int) (startTS / 60), departure.getBytes(DEFAULT_CHARSET));
    }

    private Trip(int startTSinMin, byte[] departure) {
	checkArgument(departure.length <= TicketManagerOncard.TRIP_DEPARTURE_MAX_LENGTH,
		"Description too long. Max. " + TicketManagerOncard.TRIP_DEPARTURE_MAX_LENGTH + "  bytes (UTF-8)");
	mDeparture = new String(departure).trim();
	mStartTS = startTSinMin;
    }

    /**
     * Liefert den Abfahrtsbahnhof
     * 
     * @return Bezeichnung des Abfahrtsbahnhof
     */
    public String getDeparture() {
	return mDeparture;
    }

    /**
     * Liefert den an ganzen Minuten ausgerichteten Fahrtbeginn als
     * Unix-Zeitstempel.
     * 
     * @return Fahrtbeginn
     */
    public long getStartTS() {
	return mStartTS * 60;
    }

    /**
     * Konvertiert die Fahrt in seine Darstellung als Byte-Array. Die Fahrt wird
     * dabei nicht verändert.
     * 
     * @return Darstellung der Fahrt als Byte
     */
    public byte[] toByteArray() {
	ByteBuffer bb = ByteBuffer.allocate(TicketManagerOncard.TRIP_MAX_SIZE);
	bb.putShort((short) ((mStartTS & 0xFFFF0000) >> 16));
	bb.putShort((short) (mStartTS & 0xFFFF));
	bb.put(mDeparture.getBytes(DEFAULT_CHARSET));
	while (bb.hasRemaining()) {
	    bb.put((byte) ' ');
	}
	return bb.array();
    }

    /**
     * Erzeugt eine Fahrt aus dem übergebenen Byte-Array.
     * 
     * @param data
     *            Fahrt in Byte-Repräsentation
     * @return Eingelesene Fahrt
     */
    public static Trip extractTrip(byte[] data) {
	if (TicketManagerOncard.TRIP_MIN_SIZE <= data.length) {
	    ByteBuffer bb = ByteBuffer.wrap(data);
	    short tmpTShigh = bb.getShort();
	    int tmpTS = (tmpTShigh << 16) | bb.getShort();
	    byte[] tmpDeparture = new byte[TicketManagerOncard.TRIP_DEPARTURE_MAX_LENGTH];
	    bb.get(tmpDeparture, 0, tmpDeparture.length);
	    return new Trip(tmpTS, tmpDeparture);
	} else {
	    return null;
	}
    }
}
