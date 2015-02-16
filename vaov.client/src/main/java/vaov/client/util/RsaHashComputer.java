package vaov.client.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;

import vaov.client.message.MessageService;
import vaov.remote.message.to.MessageContentTO;

public class RsaHashComputer implements HashComputer {

	MessageService messageService = new MessageService();

	/**
	 * Hashes the specified text and returns the computed digest
	 *
	 * @param messageContent
	 *            the messageContentTO to hash
	 * @return the computed hash (digest)
	 */
	@Override
	public String computeHash(MessageContentTO messageContent) {
		String marshalledMessageContent = messageService.marshalMessageContentTO(messageContent);
		return computeHash(marshalledMessageContent);
	}

	/**
	 * Computes the Hash of the specified key.
	 *
	 * @param publicKey
	 *            the key for which to compute the hash.
	 * @return the computed hash
	 *         @
	 *         if the Key is not a RSA key
	 */
	@Override
	public String computeHash(PublicKey publicKey) {
		if (!(publicKey instanceof RSAPublicKey)) {
			throw new RuntimeException("Key is not an RSAPublicKey");
		}
		RSAPublicKey pub = (RSAPublicKey) publicKey;

		byte[] input1 = pub.getModulus().toByteArray();
		byte[] input2 = pub.getPublicExponent().toByteArray();

		return computeHash(input1, input2);
	}

	private String computeHash(byte[]... input) {
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

	private String computeHash(String messageContent) {
		// make sure message ends with a new line TODO test why???
		String s = messageContent + "\n";
		return computeHash(s.getBytes(Config.getCharset()));

	}

}
