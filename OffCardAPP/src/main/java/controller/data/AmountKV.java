package controller.data;

/**
 * 
 * Klasse zum halten der Einträge für die ComboBox des Geldautomaten
 *
 */
public class AmountKV {

    /**
     * Anzuzeigender String in der ComboBox
     */
    private final String mKey;

    /**
     * Wert für den Eintrag in Cent
     */
    private final int mAmountInCent;

    public AmountKV(int amountInCent) {
	mKey = String.format("%1$,.2f€", (float) amountInCent / 100);
	mAmountInCent = amountInCent;
    }

    /**
     * Rückgabe des Schlüssels für den Eintrag
     * 
     * @return Schlüssel als String
     */
    public String getKey() {
	return mKey;
    }

    /**
     * Wert in Cent
     * 
     * @return Cent als int
     */
    public int getAmountInCent() {
	return mAmountInCent;
    }

    @Override
    public String toString() {
	return mKey;
    }
}
