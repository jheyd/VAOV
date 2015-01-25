package vaov.client.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;

import vaov.remote.message.to.MessageContentTO;

public class HashComputer {

	/**
	 * Hashes the specified text and returns the computed digest
	 *
	 * @param messageContent
	 *            the messageContentTO to hash
	 * @return the computed hash (digest)
	 */
	public static String computeHash(MessageContentTO messageContent) {
		String marshalledMessageContent = MessageTOFactory.marshalMessageContentTO(messageContent);
		return computeHash(marshalledMessageContent);
	}

	/**
	 * Computes the Hash of the specified key.
	 *
	 * @param publicKey
	 *            the key for which to compute the hash.
	 * @return the computed hash
	 * @throws KeyException
	 *             if the Key is not a RSA key
	 */
	public static String computeHash(PublicKey publicKey) throws KeyException {
		if (!(publicKey instanceof RSAPublicKey)) {
			throw new KeyException("Key is not a RSAPublicKey");
		}
		RSAPublicKey pub = (RSAPublicKey) publicKey;

		byte[] input1 = pub.getModulus().toByteArray();
		byte[] input2 = pub.getPublicExponent().toByteArray();

		return computeHash(input1, input2);
	}

	private static String computeHash(byte[]... input) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(Config.getHashAlgorithm(), Config.getProvider());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		for (byte[] singleInput : input) {
			messageDigest.update(singleInput);
		}
		byte[] val = messageDigest.digest();
		return Base64.encodeBase64String(val);
	}

	private static String computeHash(String messageContent) {
		// make sure message ends with a new line TODO test why???
		String s = messageContent + "\n";
		return computeHash(s.getBytes(Config.getCharset()));

	}

}
