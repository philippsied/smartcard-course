package suBay;

import javacard.framework.Shareable;

public interface CryptoMgr extends Shareable {
		
	public boolean isAuthorizedFor (byte appletKey);
}
