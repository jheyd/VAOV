package vaov.client.util;

import java.io.File;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import vaov.client.account.PublicKeyConverter;
import vaov.client.account.RSAPublicKeyConverter;
import vaov.client.account.model.Password;

/**
 * Config stores all those configuration parameters that are somehow chosen
 * arbitrarily.
 *
 * Currently, this file is a mess, since it also contains information that
 * should be configurable, like the URLs of resource files.
 *
 * @author arne
 *
 */
public class Config {

	/* Fixed Parameters that must not be modified, since else compatibility,
	 * safety, security and privacy may be compromised. */
	private static final String HASH_ALGORITHM = "SHA-512";

	private static final String SIGNATURE_ALGORITHM = "RSA/NONE/PKCS1Padding";

	private static final Charset CHARSET = Charset.forName("UTF8");

	private static final PublicKeyConverter PUBLIC_KEY_CONVERTER = new RSAPublicKeyConverter();

	/* KeyStore specific settings. These are only local, so changes are not
	 * fatal. */
	private static final String KEYSTORE_TYPE = "BKS";

	private static final File USER_HOME = new File(System.getProperty("user.home"));

	private static final File DATA_DIR = new File(USER_HOME, ".vaov");

	private static final File PUBLIC_KEYS_FILE = new File(DATA_DIR, "public");

	private static final File USER_KEYS_FILE = new File(DATA_DIR, "user");

	private static final Provider PROVIDER = new BouncyCastleProvider();

	static {
		Security.addProvider(getProvider());
	}

	public static Charset getCharset() {
		return CHARSET;
	}

	public static Cipher getCipher() {
		try {
			return Cipher.getInstance(SIGNATURE_ALGORITHM, getProvider());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getHashAlgorithm() {
		return HASH_ALGORITHM;
	}

	public static File getKeyStore() {
		return USER_KEYS_FILE;
	}

	public static String getKeyStoreType() {
		return KEYSTORE_TYPE;
	}

	public static Provider getProvider() {
		return PROVIDER;
	}

	public static Password getPublicKeyPassword() {
		return new Password("123456".toCharArray());
	}

	public static File getPublicKeysFile() {
		return PUBLIC_KEYS_FILE;
	}

	public static String getSignatureAlgorithm() {
		return SIGNATURE_ALGORITHM;
	}

	public static PublicKeyConverter getPublicKeyConverter() {
		return PUBLIC_KEY_CONVERTER;
	}

}
