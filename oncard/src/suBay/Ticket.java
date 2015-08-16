package suBay;

import javacard.framework.Util;

public class Ticket {
    private final short startValidityTS;
    private final byte duration;
    private final byte durationUnit;
    private final byte[] description;

    public Ticket(short startValidityTS, byte duration, byte durationUnit, byte[] description) {
	this.startValidityTS = startValidityTS;
	this.duration = duration;
	this.durationUnit = durationUnit;
	this.description = description;
    }

    /**
     * 
     * @return
     */
    public short getStartValidityTS() {
	return this.startValidityTS;
    }

    /**
     * 
     * @return
     */
    public byte getDuration() {
	return this.duration;
    }

    /**
     * 
     * @return
     */
    public byte getDurationUnit() {
	return this.durationUnit;
    }

    /**
     * 
     * @return
     */
    public byte[] getDescription() {
	return this.description;
    }

    public void fillBuffer(byte[] buffer) {
	Util.setShort(buffer, (short) 0, this.startValidityTS);
	buffer[2] = this.duration;
	buffer[3] = this.durationUnit;
	Util.arrayCopy(this.description, (short) 0, buffer, (short) 4, (short) this.description.length);
    }
}
