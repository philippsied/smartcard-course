package suBay;
public class ShortStore {
	
	private short storedValue;
	  
	public ShortStore(short initVal) {
	  this.storedValue = initVal;
	}
	  
	/**
	 *
	 *
	 */
	public short getValue() {
	  return this.storedValue;
	}
	  	
	/**
	 *
	 * return True, if adding of diff was successful
	 */
	public boolean addValue(short diff) {
		short newAmount = (short) (this.storedValue + diff);
		if (newAmount > this.storedValue) {
			this.storedValue = newAmount;
			return true;
		}
		return false;
	}
	
	/**
	 *
	 * return True, if removing of diff was successful
	 */
	public boolean subValue(short diff) {
		if (diff < this.storedValue) {
			this.storedValue -= diff;
			return true;
		}
		return false;
	}
}
