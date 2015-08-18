package controller.server;

import java.time.Instant;
import java.time.temporal.TemporalUnit;

public class TimeServer {

    /**
     * Liefert den aktuellen Unix-Zeitstempel (Sekunden seit 01.01.1970 00:00
     * UTC).
     * 
     * @return
     */
    public static long getTimestamp() {
	return Instant.now().getEpochSecond();
    }

    /**
     * Liefert den in "Anzahl Zeiteinheiten" resultierenden Unix-Zeitstempel
     * (Sekunden seit 01.01.1970 00:00 UTC).
     * 
     * @param amountToAdd
     *            Anzahl Zeiteinheiten
     * @param unit
     *            Zeiteinheit
     * @return
     */
    public static long getTimestampIn(long amountToAdd, TemporalUnit unit) {
	return Instant.now().plus(amountToAdd, unit).getEpochSecond();
    }
}
