package controller.data;

public class AmountKV {

	private String key;
	private short amountInCent;

	public AmountKV(short amount) {
	    	this.key = String.format("%1$,.2f€",(float) amount/100);
		this.amountInCent = amount;
	}

	public short getValue() {
		return amountInCent;
	}

	@Override
	public String toString() {
		return key;
	}

}
