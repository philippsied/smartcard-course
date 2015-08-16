package suBay;

import javacard.framework.Util;

public class Trip {
    private final short startTS;
    private final byte[] departure;

    /**
     * 
     * @param startTS
     * @param departure
     */
    public Trip(short startTS, byte[] departure) {
	this.startTS = startTS;
	this.departure = departure;
    }

    /**
     * 
     * @return
     */
    public short getStartTS() {
	return this.startTS;
    }

    /**
     * 
     * @return
     */
    public byte[] getDeparture() {
	return this.departure;
    }

    public void fillBuffer(byte[] buffer) {
	Util.setShort(buffer, (short) 0, this.startTS);
	Util.arrayCopy(this.departure, (short) 0, buffer, (short) 2, (short) this.departure.length);
    }
}
