package controller.server;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class TimeServer {

    public static long getTimestamp() {
	return Instant.now().toEpochMilli();
    }

    public static long getTimestampIn(long amountToAdd, TemporalUnit unit) {
	return Instant.now().plus(amountToAdd, ChronoUnit.DAYS).getEpochSecond();
    }
}
