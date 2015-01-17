package vaov.client.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;

public class HashComputer {

	/**
	 * Computes the Hash of the specified key.
	 *
	 * @param pk
	 *            the key for which to compute the hash.
	 * @return the computed hash
	 * @throws KeyException
	 *             if the Key is not a RSA key
	 */
	public static String computeHash(PublicKey pk) throws KeyException {
		if (!(pk instanceof RSAPublicKey))
			throw new KeyException("Key is not a RSAPublicKey");
		RSAPublicKey pub = (RSAPublicKey) pk;
	
		// setup MessageDigest algorithm to compute the hash
		MessageDigest d;
		try {
			d = MessageDigest.getInstance(Config.HASH_ALGORITHM,
					Config.getProvider());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		d.update(pub.getModulus().toByteArray());
		byte[] val = d.digest(pub.getPublicExponent().toByteArray());
	
		return Base64.encodeBase64String(val);
	}

}
