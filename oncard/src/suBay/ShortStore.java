package suBay;

/**
 * Datenklasse zur Verwaltung eines Short-Wertes
 *
 */
public class ShortStore {
	
	/**
	 * Zu speichernder Wert
	 */
	private short storedValue;
	  
	public ShortStore(short initVal) {
	  this.storedValue = initVal;
	}
	  

	public short getValue() {
	  return this.storedValue;
	}
	  	
	/**
	 *
	 * return True, wenn hinzufuegen erfolgreich war
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
	 * return True, wenn loeschen erfolgreich war
	 */
	public boolean subValue(short diff) {
		if (diff < this.storedValue) {
			this.storedValue -= diff;
			return true;
		}
		return false;
	}
}
