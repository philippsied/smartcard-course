package suBay;

import javacard.framework.Shareable;

public interface CryptoStore extends Shareable {
	
	public byte[] encrypt(byte[] data);
}
