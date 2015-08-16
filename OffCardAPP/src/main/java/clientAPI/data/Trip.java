package clientAPI.data;

import static com.google.common.base.Preconditions.checkArgument;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import clientAPI.impl.OncardAPI.TicketManagerOncard;

public class Trip {

    private final short mStartTS;
    private final String mDeparture;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 
     * @param departure
     *            Abfahrtsbahnhof
     * @param startTS
     *            Unix-Zeitstempel für den Zeitpunkt des Fahrtbeginnes.
     *            Millisekunden und Sekunden werden aus Platzgründen
     *            abgeschnitten.
     */
    public Trip(long startTS, String departure) {
	// Zeitstempel in minutengenaue Speicherung überführen
	this((short) (startTS / (60 * 1000)), departure.getBytes(DEFAULT_CHARSET));
    }

    /**
     * 
     * @param departure
     *            Abfahrtsbahnhof
     * @param startTS
     *            Unix-Zeitstempel in Minuten
     */
    public Trip(short startTS, byte[] departure) {
	checkArgument(departure.length <= TicketManagerOncard.TRIP_DEPARTURE_LENGTH,
		"Description too long. Max. " + TicketManagerOncard.TRIP_DEPARTURE_LENGTH + "  bytes (UTF-8)");
	mDeparture = new String(departure).trim();
	mStartTS = startTS;
    }

    public String getDeparture() {
	return mDeparture;
    }

    public long getStartTS() {
	return mStartTS * 60 * 1000;
    }

    public byte[] toByteArray() {
	ByteBuffer bb = ByteBuffer.allocate(TicketManagerOncard.TRIP_SIZE);
	bb.putShort(mStartTS);
	bb.put(mDeparture.getBytes(DEFAULT_CHARSET));
	while (bb.hasRemaining()) {
	    bb.putChar(' ');
	}
	return bb.array();
    }

}
