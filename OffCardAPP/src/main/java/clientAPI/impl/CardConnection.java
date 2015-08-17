package clientAPI.impl;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public final class CardConnection {

    private CardChannel mChannel;

    public CardConnection(Card card) {
	mChannel = card.getBasicChannel();
    }

    public ResponseAPDU select(byte[] aid) throws CardException {
	return sendAPDU(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
    }

    public ResponseAPDU sendAPDU(CommandAPDU cmd) throws CardException {
	try {
	    return mChannel.transmit(cmd);
	} catch (IllegalStateException e) {
	    throw new CardException("Nicht zur Karte verbunden");
	} catch (NullPointerException e) {
	    throw new CardException("Keine Antwort erhalten");
	}

    }

    public ResponseAPDU sendAPDU(CommandHeader header, byte[] data, short expectedResponseLength)
	    throws IllegalArgumentException, CardException {
	if (header.definedLC.isPresent()) {
	    if (header.definedLC.get() != data.length)
		throw new IllegalArgumentException("data length deviates from defined LC");
	}
	if (header.definedLE.isPresent()) {
	    if (header.definedLE.get() != expectedResponseLength)
		throw new IllegalArgumentException("expected LE deviates from defined LE");
	}
	switch (header.type) {
	case NoLC_NoLE:
	    return sendAPDU(new CommandAPDU(header.CLA, header.INS, header.P1, header.P2));
	case NoLC_LE:
	    return sendAPDU(new CommandAPDU(header.CLA, header.INS, header.P1, header.P2, expectedResponseLength));
	case LC_NoLE:
	    return sendAPDU(new CommandAPDU(header.CLA, header.INS, header.P1, header.P2, data));
	case LC_LE:
	    return sendAPDU(
		    new CommandAPDU(header.CLA, header.INS, header.P1, header.P2, data, expectedResponseLength));
	default:
	    return null;
	}
    }

    public ResponseAPDU sendAPDU(CommandHeader header, byte[] data) throws IllegalArgumentException, CardException {
	if (header.definedLC.isPresent()) {
	    if (header.definedLC.get() != data.length)
		throw new IllegalArgumentException("data length deviates from defined LC");
	}

	switch (header.type) {

	case NoLC_LE:
	    if (header.definedLE.isPresent()) {
		return sendAPDU(new CommandAPDU(header.CLA, header.INS, header.P1, header.P2, header.definedLE.get()));
	    }
	case NoLC_NoLE:
	    return sendAPDU(new CommandAPDU(header.CLA, header.INS, header.P1, header.P2));
	case LC_LE:
	    if (header.definedLE.isPresent()) {
		return sendAPDU(
			new CommandAPDU(header.CLA, header.INS, header.P1, header.P2, data, header.definedLE.get()));
	    }
	case LC_NoLE:
	    return sendAPDU(new CommandAPDU(header.CLA, header.INS, header.P1, header.P2, data));

	default:
	    return null;
	}
    }
}
