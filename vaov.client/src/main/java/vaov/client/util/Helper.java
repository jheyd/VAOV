package vaov.client.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import vaov.remote.services.KeyId;

/**
 * This class is the main work horse. It contains methods that compute hashes or
 * transform keys to Strings etc.
 *
 * All methods are static. Hence, this class needs not be instantiated.
 *
 * @author arne
 *
 */
public class Helper {

	/**
	 * Reads a line of a input file. The line must have the format:
	 * <code>[identifier]:[value]</code> The identifier must not contain any
	 * colons. The value must not contain any colons. If it does not have this
	 * format, an IllegalFormatException is thrown. Whitespace around value is
	 * trimmed away.
	 *
	 * @param identifier
	 *            the identifier with which the line has to start
	 * @param text
	 *            the text of the complete line
	 * @return the value part of the line (the text following the colon)
	 * @throws IllegalFormatException
	 */
	public static String read(String identifier, String text)
			throws IllegalFormatException {
		if (identifier.contains(":"))
			throw new IllegalArgumentException(
					"Identifier must contain no colon!");
		if (!text.startsWith(identifier + ":"))
			throw new IllegalFormatException("Expected \"" + identifier + ":\"");
		String[] textSplit = text.split(":");
		if (textSplit.length != 2)
			throw new IllegalFormatException("String after \"" + identifier
					+ ":\" must contain no \":\"");
		return textSplit[1].trim();
	}

	/**
	 * Checks, if the given hash is really the hash of the specified key.
	 *
	 * @param pk
	 *            the key to verify
	 * @param hash
	 *            the hash to verify
	 * @throws KeyException
	 *             if the hash doesn't match to the key.
	 */
	public static void verifyKey(PublicKey pk, String hash) throws KeyException {
		String encoded = HashComputer.computeHash(pk);
		if (!encoded.equals(hash))
			throw new KeyException("Key does not match to hash: " + hash);
	}

	public static KeyPair generateKeyPair() {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		kpg.initialize(4096);
		KeyPair generateKeyPair = kpg.generateKeyPair();
		return generateKeyPair;
	}

	public static KeyId generateKeyId(KeyPair keyPair) {
		return new KeyId(HashComputer.computeHash(keyPair.getPublic()));
	}

}
