package vaov.client.util;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.apache.commons.codec.binary.Base64;

import vaov.remote.account.to.PublicKeyTO;

public class PublicKeyConverter {

	/**
	 * Creates a public key from modulus and exponent given as strings in Base64
	 * encoding. For precise description on the format of the strings see the
	 * protocol documentation. A RSA key is created.
	 *
	 * @param modulus
	 *            the modulus of the RSA key.
	 * @param exponent
	 *            the exponent of the RSA key.
	 * @return the loaded public key
	 * @throws IllegalFormatException
	 *             if something is not in the format as expected
	 */
	public static PublicKey readPublicKey(String modulus, String exponent) {
		RSAPublicKeySpec spec;
		byte[] enc_modulus = Base64.decodeBase64(modulus);
		byte[] enc_exponent = Base64.decodeBase64(exponent);
		BigInteger int_modulus = new BigInteger(enc_modulus);
		BigInteger int_exponent = new BigInteger(enc_exponent);
		if (int_modulus.compareTo(BigInteger.ZERO) <= 0)
			throw new IllegalFormatException("Modulus must be positive");
		if (int_exponent.compareTo(BigInteger.ZERO) <= 0)
			throw new IllegalFormatException("Exponent must be positive");
		spec = new RSAPublicKeySpec(int_modulus, int_exponent);
		PublicKey pk;
		try {
			// TODO: specify security provider
			KeyFactory fact = KeyFactory.getInstance("RSA", Config.getProvider());
			pk = fact.generatePublic(spec);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("No security provider for RSA algorithm", e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalFormatException("Unable to generate public key", e);
		}

		return pk;
	}

	public static PublicKeyTO writePublicKey(PublicKey pk) {
		String[] lines = pk.toString().split("\n");
		String modulus = lines[1].split(" ")[3];
		String exponent = lines[2].split(" ")[4];
		return new PublicKeyTO(modulus, exponent);
	}

}
