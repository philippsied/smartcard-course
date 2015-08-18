package suBay;

import javacard.framework.Util;

public class Ticket {
    private final short startValidityTS_h;
	private final short startValidityTS_l;
    private final short expireValidityTS_h;
	private final short expireValidityTS_l;
    private final byte[] description;

    public Ticket(short startValidityTS_h, short startValidityTS_l, short expireValidityTS_h, short expireValidityTS_l, byte[] description) {
	this.startValidityTS_h = startValidityTS_h;
	this.startValidityTS_l = startValidityTS_l;
	this.expireValidityTS_h = expireValidityTS_h;
	this.expireValidityTS_l = expireValidityTS_l;
	this.description = description;
    }


    /**
     * 
     * @return
     */
    public byte[] getDescription() {
	return this.description;
    }

    public void fillBuffer(byte[] buffer) {
	Util.setShort(buffer, (short) 0, this.startValidityTS_h);
	Util.setShort(buffer, (short) 2, this.startValidityTS_l);
	Util.setShort(buffer, (short) 4, this.expireValidityTS_h);
	Util.setShort(buffer, (short) 6, this.expireValidityTS_l);
	Util.arrayCopy(this.description, (short) 0, buffer, (short) 8, (short) this.description.length);
    }
}
