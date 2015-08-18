package suBay;

import javacard.framework.Util;

public class Trip {
    private final short startTS_h;
    private final short startTS_l;
    private final byte[] departure;

    /**
     * 
     * @param startTS
     * @param departure
     */
    public Trip(short startTS_h, short startTS_l, byte[] departure) {
	this.startTS_h = startTS_h;
	this.startTS_l = startTS_l;
	this.departure = departure;
    }

   
    /**
     * 
     * @return
     */
    public byte[] getDeparture() {
	return this.departure;
    }

    public void fillBuffer(byte[] buffer) {
	Util.setShort(buffer, (short) 0, this.startTS_h);
	Util.setShort(buffer, (short) 2, this.startTS_l);
	Util.arrayCopy(this.departure, (short) 0, buffer, (short) 4, (short) this.departure.length);
    }
}
