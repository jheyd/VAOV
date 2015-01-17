package vaov.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V1CertificateGenerator;

public class KeystoreService {

	public static KeyPair loadKeyPair(String keyId, char[] password)
			throws KeyStoreException, IOException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException,
			UnrecoverableKeyException, KeyException {
		KeyStore ks = KeyStore.getInstance(Config.getKeyStoreType(),
				Config.getProvider());
		ks.load(new FileInputStream(Config.getKeyStore()), password);
		PublicKey publicKey = (PublicKey) ks.getKey(keyId
				+ Config.ACCOUNT_ALIAS_PUBLIC, password);
		PrivateKey privateKey = (PrivateKey) ks.getKey(keyId
				+ Config.ACCOUNT_ALIAS_PRIVATE, password);

		if (publicKey == null || privateKey == null)
			throw new KeyException("Key with id \"" + keyId
					+ "\" does not exist");

		return new KeyPair(publicKey, privateKey);
	}

	public static void storeKey(String keyId, char[] password, KeyPair keys)
			throws KeyStoreException, IOException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException,
			CertificateEncodingException {
		KeyStore ks = KeyStore.getInstance(Config.getKeyStoreType(),
				Config.getProvider());
		File keyStoreFile = Config.getKeyStore();
		if (keyStoreFile.exists())
			if (keyStoreFile.isFile())
				ks.load(new FileInputStream(keyStoreFile), password);
			else
				throw new RuntimeException(
						"a directory with the name of the keystore exists");
		else
			ks.load(null, null);
		ks.setKeyEntry(keyId + Config.ACCOUNT_ALIAS_PUBLIC, keys.getPublic(),
				password, null);

		/*
		 * We need a stupid certificate to store the key, so just create a
		 * self-signed one.
		 */
		Date startDate = Date.valueOf("2000-01-01"); // time from which
														// certificate is
														// valid
		Date expiryDate = Date.valueOf("3000-01-01"); // time after which
														// certificate is
														// not valid
		BigInteger serialNumber = new BigInteger("42"); // serial number for
														// certificate

		// method is deprecated, for production use, we may need to include
		// the CertificateBuilder of the full bouncy-castle lib
		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
		X500Principal dnName = new X500Principal("CN=Stupid CA Certificate");

		certGen.setSerialNumber(serialNumber);
		certGen.setIssuerDN(dnName);
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		certGen.setSubjectDN(dnName); // note: same as issuer
		certGen.setPublicKey(keys.getPublic());
		certGen.setSignatureAlgorithm("MD2withRSA");

		X509Certificate cert = null;
		try {
			cert = certGen.generate(keys.getPrivate(), "BC");
		} catch (InvalidKeyException | IllegalStateException
				| NoSuchProviderException | SignatureException e) {
			throw new RuntimeException(e);
		}

		Certificate[] certs = { cert };

		ks.setKeyEntry(keyId + Config.ACCOUNT_ALIAS_PRIVATE, keys.getPrivate(),
				password, certs);
		ks.store(new FileOutputStream(keyStoreFile), password);
	}

	/**
	 * Creates a new empty Keystore. If a keystore already existed, the old
	 * keystore is overwritten.
	 *
	 * @param password
	 *            the password with which to protect the keystore.
	 * @throws KeyException
	 */
	public static void initKeyStore(char[] password) throws KeyException {
		try {
			KeyStore ks = KeyStore.getInstance(Config.getKeyStoreType(),
					Config.getProvider());
			ks.load(null, null); // initialized default KeyStore
			ks.store(new FileOutputStream(Config.getKeyStore()), password);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (CertificateException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new KeyException("Cannot create KeyStore", e);
		} catch (IOException e) {
			throw new KeyException("Cannot create KeyStore", e);
		}
	}

}
