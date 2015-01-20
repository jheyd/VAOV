package vaov.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

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
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public static File getKeyStore() {
		return new File(INSTANCE.get("keystore"));
	}

	public static String getKeyStoreType() {
		return KEYSTORE_TYPE;
	}

	public static Provider getProvider() {
		return provider;
	}

	public static char[] getPublicKeyPassword() {
		return "password".toCharArray();
	}

	/* Fixed Parameters that must not be modified, since else compatibility,
	 * safety, security and privacy may be compromised. */
	public static final String HASH_ALGORITHM = "SHA-512";

	public static final String SIGNATURE_ALGORITHM = "RSA/NONE/PKCS1Padding";

	public static final String CHARSET = "UTF8";

	/* KeyStore specific settings. These are only local, so changes are not
	 * fatal. */
	public static final String KEYSTORE_TYPE = "BKS";

	private static final Provider provider = new BouncyCastleProvider();

	/**
	 * The properties file will always be stored in this file
	 */
	private static final String configFile = "./config";

	private static final Config INSTANCE = new Config();

	/**
	 * All indivdual configuration settings are stored in config. Every change
	 * in config shall be immediately reflected in the corresponding local
	 * properties file.
	 */
	private Properties config;

	public Config() {
		config = new Properties();
		try {
			config.load(new FileInputStream(configFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("It seems you f***ed up your installation", e);
		} catch (IOException e) {
			throw new RuntimeException("It seems you f***ed up your installation", e);
		}
		Security.addProvider(getProvider());
	}

	public String get(String key) {
		return config.getProperty(key);
	}

	public void set(String key, String value) {
		config.setProperty(key, value);
		try {
			FileOutputStream out = new FileOutputStream(configFile);
			config.store(out, "Configuration updated on " + new Date() + " by user");
		} catch (IOException e) {
			throw new RuntimeException("It seems you f***ed up your installation", e);
		}
	}
}
