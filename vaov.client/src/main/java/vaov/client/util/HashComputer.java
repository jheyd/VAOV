package vaov.client.util;

import java.security.PublicKey;

import vaov.remote.message.to.MessageContentTO;

public interface HashComputer {

	/**
	 * Hashes the specified text and returns the computed digest
	 *
	 * @param messageContent
	 *            the messageContentTO to hash
	 * @return the computed hash (digest)
	 */
	public abstract String computeHash(MessageContentTO messageContent);

	/**
	 * Computes the Hash of the specified key.
	 *
	 * @param publicKey
	 *            the key for which to compute the hash.
	 * @return the computed hash
	 *         @
	 *         if the Key is not a RSA key
	 */
	public abstract String computeHash(PublicKey publicKey);

}