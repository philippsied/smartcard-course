package clientAPI.data;

import static com.google.common.base.Preconditions.checkArgument;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import clientAPI.impl.OncardAPI.TicketManagerOncard;

public class Trip {

    private final int mStartTS;
    private final String mDeparture;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 
     * @param departure
     *            Abfahrtsbahnhof
     * @param startTS
     *            Unix-Zeitstempel für den Zeitpunkt des Fahrtbeginnes. Sekunden
     *            werden aus Platzgründen abgeschnitten.
     */
    public Trip(long startTS, String departure) {
	// Zeitstempel in minutengenaue Speicherung überführen
	this((int) (startTS / 60), departure.getBytes(DEFAULT_CHARSET));
    }

    /**
     * 
     * @param departure
     *            Abfahrtsbahnhof
     * @param startTSinMin
     *            Unix-Zeitstempel in Minuten
     */
    private Trip(int startTSinMin, byte[] departure) {
	checkArgument(departure.length <= TicketManagerOncard.TRIP_DEPARTURE_MAX_LENGTH,
		"Description too long. Max. " + TicketManagerOncard.TRIP_DEPARTURE_MAX_LENGTH + "  bytes (UTF-8)");
	mDeparture = new String(departure).trim();
	mStartTS = startTSinMin;
    }

    public String getDeparture() {
	return mDeparture;
    }

    public long getStartTS() {
	return mStartTS * 60;
    }

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
