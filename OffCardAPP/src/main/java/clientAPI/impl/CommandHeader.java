package clientAPI.impl;

import java.util.Optional;

/**
 * Datenklasse zur Festlegung der APDU eines Kommandos. Optional kann die Länge
 * oder das erwartete Resultat im Vorfeld festgesetzt und über
 * {@code CardConnection} erzwungen werden.
 *
 */
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
     * @return Byte-Array bestehende aus CLA, INS, P1, P2
     */
    public byte[] getHeader() {
	return new byte[] { CLA, INS, P1, P2 };
    }

    public static enum CmdType {
	/**
	 * Case 1: Keine Command Daten, Keine Response Daten
	 */
	NoLC_NoLE, /**
		    * Case 2: Keine Command Daten, Erwarte Daten in Länge von LE
		    */
	NoLC_LE, /**
		  * Case 3: Command Daten mit LC Länge, Keine Response Daten
		  */
	LC_NoLE, /**
		  * Case 4: Command Daten mit LC Länge, Erwarte Daten in Länge
		  * von LE
		  */
	LC_LE;
    }
}
