package vaov.client.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import vaov.remote.message.to.MessageContentTO;

public class DigestComputer {

	private static String computeDigest(String marshalledMessageContent) {
		// make sure message ends with a new line
		if (!marshalledMessageContent.endsWith("\n")) {
			marshalledMessageContent = marshalledMessageContent + "\n";
		}
		MessageDigest d;
		try {
			d = MessageDigest.getInstance(Config.HASH_ALGORITHM, Config.getProvider());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] val;
		try {
			val = d.digest(marshalledMessageContent.getBytes(Config.CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return Base64.encodeBase64String(val);
	}

	/**
	 * Hashes the specified text and returns the computed digest
	 *
	 * @param messageContent
	 *            the text to hash
	 * @return the computed hash (digest)
	 */
	public static String computeDigest(MessageContentTO messageContent) {
		String marshalledMessageContent = MessageTOFactory.marshalMessageContentTO(messageContent);
		return computeDigest(marshalledMessageContent);
	}

}
