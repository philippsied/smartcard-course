package controller.data;

public class AmountKV {

	private String key;
	private short amountInCent;

	public AmountKV(short amount) {
		this.key = String.valueOf(amount) + "Cent";
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
