package clientAPI.impl.OncardAPI;

import clientAPI.impl.CommandHeader;
import clientAPI.impl.CommandHeader.CmdType;

public interface PersonalDataOncard {

    /**
     * Oncard AID
     */
    public static final byte[] AID = { (byte) 0xFD, 'u', 'B', 'a', 'y', 'P', 'e', 'r', 's', 'S', 't', 'o', 'r', 'e' };

    public final static CommandHeader SET_FNAME = new CommandHeader((byte) 0xE0, (byte) 0x1A, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    public final static CommandHeader GET_FNAME = new CommandHeader((byte) 0xE0, (byte) 0x1B, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE);

    public final static CommandHeader SET_SURNAME = new CommandHeader((byte) 0xE0, (byte) 0x2A, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    public final static CommandHeader GET_SURNAME = new CommandHeader((byte) 0xE0, (byte) 0x2B, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE);

    public final static CommandHeader SET_BDAY = new CommandHeader((byte) 0xE0, (byte) 0x3A, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    public final static CommandHeader GET_BDAY = new CommandHeader((byte) 0xE0, (byte) 0x3B, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE);

    public final static CommandHeader SET_LOCATION = new CommandHeader((byte) 0xE0, (byte) 0x4A, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    public final static CommandHeader GET_LOCATION = new CommandHeader((byte) 0xE0, (byte) 0x4B, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE);

    public final static CommandHeader SET_STREET = new CommandHeader((byte) 0xE0, (byte) 0x5A, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    public final static CommandHeader GET_STREET = new CommandHeader((byte) 0xE0, (byte) 0x5B, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE);

    public final static CommandHeader SET_PHONENR = new CommandHeader((byte) 0xE0, (byte) 0x6A, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);

    public final static CommandHeader GET_PHONENR = new CommandHeader((byte) 0xE0, (byte) 0x6B, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE);

    public final static CommandHeader SET_PHOTO = new CommandHeader((byte) 0xE0, (byte) 0x7A, (byte) 0x00, (byte) 0x00, CmdType.LC_NoLE);
    
    public final static CommandHeader GET_PHOTO = new CommandHeader((byte) 0xE0, (byte) 0x7B, (byte) 0x00, (byte) 0x00, CmdType.NoLC_LE);
}
