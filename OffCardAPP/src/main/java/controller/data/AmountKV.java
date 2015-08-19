package controller.data;

public class AmountKV {

    private final String mKey;
    private final int mAmountInCent;

    public AmountKV(int amountInCent) {
	mKey = String.format("%1$,.2f€", (float) amountInCent / 100);
	mAmountInCent = amountInCent;
    }
 
    
    public String getKey() {
        return mKey;
    }
    
    public int getAmountInCent() {
        return mAmountInCent;
    }


    @Override
    public String toString() {
	return mKey;
    }   
}
