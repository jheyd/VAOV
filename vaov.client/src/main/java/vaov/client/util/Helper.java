package vaov.client.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

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
			KeyFactory fact = KeyFactory.getInstance("RSA",
					Config.getProvider());
			pk = fact.generatePublic(spec);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(
					"No security provider for RSA algorithm", e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalFormatException("Unable to generate public key", e);
		}

		return pk;
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

	/**
	 * Verifies a signature. Decodes <code>signature</code> using the specified
	 * public key and checks if the result equals <code>digest</code>.
	 *
	 * @param digest
	 *            the digest to verify against
	 * @param signature
	 *            the signature string to decode
	 * @param pk
	 *            the public key for decoding
	 * @return true iff verified.
	 * @throws KeyException
	 *             if the given key is not a RSA-key
	 */
	public static boolean verifySignature(String digest, String signature,
			PublicKey pk) throws KeyException {
		if (!(pk instanceof RSAPublicKey))
			throw new KeyException("Key is not a RSAPublicKey");
		byte[] val;
		try {
			Cipher c = Cipher.getInstance(Config.SIGNATURE_ALGORITHM,
					Config.getProvider());
			c.init(Cipher.DECRYPT_MODE, pk);
			byte[] decoded = Base64.decodeBase64(signature);
			val = c.doFinal(decoded);
			return digest.equals(new String(val, Config.CHARSET));
		} catch (InvalidKeyException e) {
			throw new KeyException("Key is not a valid Key", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Writes a public key with the specified string builder. This is the
	 * inverse operation of {@link #readPublicKey(String, String)}. It first
	 * writes a line "Modulus: &lt;modulus&gt;" and then writes a line
	 * "Exponent: &lt;exponent&gt;".
	 *
	 * @param builder
	 *            the builder to write the result into
	 * @param pk
	 *            the public key to write
	 * @throws KeyException
	 *             if the key is not a RSA-Key
	 */
	public static void writePublicKey(StringBuilder builder, PublicKey pk)
			throws KeyException {
		if (!(pk instanceof RSAPublicKey))
			throw new KeyException("Key is not a RSAPublicKey");
		RSAPublicKey pub = (RSAPublicKey) pk;
		builder.append("Modulus: ");
		String modulus = Base64.encodeBase64String(pub.getModulus()
				.toByteArray());
		builder.append(modulus);
		builder.append("\n");
		builder.append("Exponent: ");
		String exponent = Base64.encodeBase64String(pub.getPublicExponent()
				.toByteArray());
		builder.append(exponent);
		builder.append("\n");
	}

}
