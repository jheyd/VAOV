package vaov.client.util;

import java.io.File;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

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

	public static Cipher getCipher() {
		try {
			return Cipher.getInstance(SIGNATURE_ALGORITHM, getProvider());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public static File getKeyStore() {
		return USER_KEYS_FILE;
	}

	public static File getPublicKeysFile() {
		return PUBLIC_KEYS_FILE;
	}

	public static String getKeyStoreType() {
		return KEYSTORE_TYPE;
	}

	public static Provider getProvider() {
		return PROVIDER;
	}

	public static char[] getPublicKeyPassword() {
		return "password".toCharArray();
	}

	/* Fixed Parameters that must not be modified, since else compatibility,
	 * safety, security and privacy may be compromised. */
	public static final String HASH_ALGORITHM = "SHA-512";

	public static final String SIGNATURE_ALGORITHM = "RSA/NONE/PKCS1Padding";

	public static final Charset CHARSET = Charset.forName("UTF8");

	/* KeyStore specific settings. These are only local, so changes are not
	 * fatal. */
	public static final String KEYSTORE_TYPE = "BKS";

	public static final Provider PROVIDER = new BouncyCastleProvider();

	private static final File USER_HOME = new File(System.getProperty("user.home"));

	private static final File DATA_DIR = new File(USER_HOME, ".vaov");

	private static final File PUBLIC_KEYS_FILE = new File(DATA_DIR, "public");

	private static final File USER_KEYS_FILE = new File(DATA_DIR, "user");

	static {
		Security.addProvider(getProvider());
	}

}
