package suBay;

import javacard.framework.Shareable;

/**
 * Schnittstelle zur Festlegung des Zugriffes auf das Crypto-Manager-Applet
 */
 public interface CryptoMgr extends Shareable {
		
	/**
	 * Prueft ob aktuell eine Berechtigung fuer das angefragte Applet mit dem appletKey existiert.
	 */
	public boolean isAuthorizedFor (byte appletKey);
}
