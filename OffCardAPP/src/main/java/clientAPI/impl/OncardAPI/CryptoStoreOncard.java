package clientAPI.impl.OncardAPI;

public interface CryptoStoreOncard {

	/**
	 * Authenticate on smartcard for bonus credit store. Be used to initiate the challenge-response-authentication or to send the response  <br>
	 * <pre>
	 * CLA: 0xE0 <br>
	 * INS: 0x00 <br>
	 * P1: stub for parameter. See {@link AUTHENTICATE_PARAM_STARTCHALLENGE}  or {@link AUTHENTICATE_PARAM_RESPONSE} <br>
	 * P2: 0x00 <br>
	 * LC: variable <br>
	 * Data-Stub: variable <br>
	 * </pre>
	 */
	public final static byte[] AUTHENTICATE = { (byte) 0xE0, 0x08, 0x00, 0x00 };
	
	/**
	 * Parameter for {@link AUTHENTICATE} to indicate, that the challenge-response-authentication can begin.
	 */
	public final static byte AUTHENTICATE_PARAM_STARTCHALLENGE = (byte) 0xFF;
	
	/**
	 * Parameter for {@link AUTHENTICATE} to send the calculated response.
	 */
	public final static byte AUTHENTICATE_PARAM_RESPONSE = (byte) 0xAA;
}
