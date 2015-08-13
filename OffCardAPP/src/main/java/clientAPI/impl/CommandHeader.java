package clientAPI.impl;

import java.util.Optional;

public class CommandHeader {
	public final byte CLA;
	public final byte INS;
	public final byte P1;
	public final byte P2;
	public final CmdType type;
	public final Optional<Short> definedLC;
	public final Optional<Short> definedLE;

	private CommandHeader(byte CLA, byte INS, byte P1, byte P2, Short definedLC, Short definedLE, CmdType type) {
		this.CLA = CLA;
		this.INS = INS;
		this.P1 = P1;
		this.P2 = P2;
		this.type = type;
		this.definedLC = Optional.ofNullable(definedLC);
		this.definedLE = Optional.ofNullable(definedLE);
	}

	public CommandHeader(byte CLA, byte INS, byte P1, byte P2, CmdType type) {
		this(CLA, INS, P1, P2, null, null, type);
	}

	public CommandHeader(byte CLA, byte INS, byte P1, byte P2, short definedLC, CmdType type) {
		this(CLA, INS, P1, P2, definedLC, null, type);
	}

	public CommandHeader(byte CLA, byte INS, byte P1, byte P2, CmdType type, short definedLE) {
		this(CLA, INS, P1, P2, null, definedLE, type);
	}

	public CommandHeader(byte CLA, byte INS, byte P1, byte P2, short definedLC, CmdType type, short definedLE) {
		this(CLA, INS, P1, P2, definedLC, definedLE, type);
	}

	/**
	 * @return byte array consist of CLA, INS, P1, P2
	 */
	public byte[] getHeader() {
		return new byte[] { CLA, INS, P1, P2 };
	}

	public static enum CmdType {
		/**
		 * Case 1: No command data, No response data
		 */
		NoLC_NoLE, /**
					 * Case 2: No command data, Expect response data of length
					 * LE
					 */
		NoLC_LE, /**
					 * Case 3: Command data with length LC, No response data
					 */
		LC_NoLE, /**
					 * Case 4: Command data with length LC, Expect response data
					 * of length LE
					 */
		LC_LE;
	}
}
