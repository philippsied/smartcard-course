package controller.data;

public class AmountKV {

    private final String mKey;
    private final short mAmountInCent;

    public AmountKV(short amountInCent) {
	mKey = String.format("%1$,.2f€", (float) amountInCent / 100);
	mAmountInCent = amountInCent;
    }
 
    
    public String getKey() {
        return mKey;
    }
    
    public short getAmountInCent() {
        return mAmountInCent;
    }


    @Override
    public String toString() {
	return mKey;
    }   
}
