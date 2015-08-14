package suBay;
public interface CryptoStore extends Shareable {
	
	public byte[] encrypt(byte[] data);
}
