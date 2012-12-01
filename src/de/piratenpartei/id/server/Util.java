package de.piratenpartei.id.server;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Util {
	public static CharBuffer decodeBuf(ByteBuffer buf) {
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer charBuffer = null;
		try {
			charBuffer = decoder.decode(buf);
		} catch (CharacterCodingException e) {
			System.out.println("Error: Could not decode ByteBuffer");
		}
		
		return charBuffer;
	}
}
