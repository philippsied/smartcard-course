package suBay;
public class Trip {
	private final byte[] departure;
	private final int startTS;
	
	public Trip(byte[] departure, int startTS) {
		this.departure = departure;
		this.startTS = startTS;
	}
	
	/**
	 *
	 *
	 */
	public byte[] getDeparture() {
	  return this.departure;
	}
	
	/**
	 *
	 *
	 */
	public int getStartTS() {
	  return this.startTS;
	} 
}
