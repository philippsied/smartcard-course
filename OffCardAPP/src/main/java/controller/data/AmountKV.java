package controller.data;

public class AmountKV {
	
	private String key;
	private int amount;
	
	public AmountKV(int amount) {
		this.key = String.valueOf(amount) + "€";
		this.amount = amount;
	}

	public int getValue() {
		return amount;
	}

	@Override
	public String toString() {
		return key;
	}

}
