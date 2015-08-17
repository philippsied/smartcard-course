package controller.data;

import clientAPI.data.Ticket;
import clientAPI.data.Ticket.DurationUnit;

public class TicketEntry {
    
    private final byte mDuration;
    private final DurationUnit mDurationUnit;
    private final String mDescription;
    private final short mAmount;
    private final short mPoints;
    
    public TicketEntry(byte mDuration, DurationUnit mDurationUnit, String mDescription, short mAmount, short mPoints) {
	super();
	this.mDuration = mDuration;
	this.mDurationUnit = mDurationUnit;
	this.mDescription = mDescription;
	this.mAmount = mAmount;
	this.mPoints = mPoints; 
    }

    public byte getmDuration() {
        return mDuration;
    }

    public DurationUnit getmDurationUnit() {
        return mDurationUnit;
    }

    public String getmDescription() {
        return mDescription;
    }

    public short getCent() {
        return mAmount;
    }
    
    public String getEurString() {
	return String.format("%1$,.2f€",(float) mAmount/100);
    }
    
    public short getmPoints() {
        return mPoints;
    }

    public Ticket createTicket(){
	return new Ticket(System.currentTimeMillis(), mDuration, mDurationUnit, mDescription);
    }

    @Override
    public String toString() {
	//return mDescription + String.format(" (%1$,.2f€)", mAmount);
	return mDescription;
    }
    
}
