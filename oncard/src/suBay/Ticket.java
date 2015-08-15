package suBay;
public class Ticket {
	private final byte[] description;
	private final int startValidityTS;
	private final byte duration;
	private final byte durationUnit;
	
	public Ticket(byte[] description, int startValidityTS, byte duration, byte durationUnit) {
		this.description = description;
		this.startValidityTS = startValidityTS;
		this.duration = duration;
		this.durationUnit = durationUnit;
	}
	
	/**
	 *
	 *
	 */
	public byte[] getDescription() {
	  return this.description;
	}
	
	/**
	 *
	 *
	 */
	public int getStartValidityTS() {
	  return this.startValidityTS;
	} 
	
	/**
	 *
	 *
	 */
	public byte getDuration() {
	  return this.duration;
	}
	
	/**
	 *
	 *
	 */
	public byte getDurationUnit() {
	  return this.durationUnit;
	} 
}
