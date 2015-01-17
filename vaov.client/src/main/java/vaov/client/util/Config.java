package vaov.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

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

	/*
	 * Fixed Parameters that must not be modified, since else compatibility,
	 * safety, security and privacy may be compromised.
	 */
	public static final String HASH_ALGORITHM = "SHA-512";
	public static final String SIGNATURE_ALGORITHM = "RSA/NONE/PKCS1Padding";
	public static final String CHARSET = "UTF8";

	/*
	 * KeyStore specific settings. These are only local, so changes are not
	 * fatal.
	 */
	public static final String KEYSTORE_TYPE = "BKS";
	public static final String ACCOUNT_ALIAS_PRIVATE = "_privatekey";
	public static final String ACCOUNT_ALIAS_PUBLIC = "_publickey";

	private static final Provider provider = new BouncyCastleProvider();

	/**
	 * The properties file will always be stored in this file
	 */
	private static final String configFile = "./config";

	private static final Config INSTANCE = new Config();

	public static File getKeyStore() {
		return new File(INSTANCE.get("keystore"));
	}

	public static String getKeyStoreType() {
		return KEYSTORE_TYPE;
	}

	public static String getLegitimateChecksum() {
		return INSTANCE.get("legitimateChecksum"); // TODO: THIS IS NOT SAFE!
	}

	public static Provider getProvider() {
		return provider;
	}

	public static URL getVerifiedAccounts() {
		try {
			return new URL(INSTANCE.get("verified"));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

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
			throw new RuntimeException(
					"It seems you f***ed up your installation", e);
		} catch (IOException e) {
			throw new RuntimeException(
					"It seems you f***ed up your installation", e);
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
			config.store(out, "Configuration updated on " + new Date()
					+ " by user");
		} catch (IOException e) {
			throw new RuntimeException(
					"It seems you f***ed up your installation", e);
		}
	}

	public static char[] getPublicKeyPassword() {
		return "password".toCharArray();
	}
}
