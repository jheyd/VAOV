package vaov.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
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
import java.util.Optional;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V1CertificateGenerator;

public class KeystoreService {

	public static KeyPair loadKeyPair(String keyId, char[] password) {
		Optional<PublicKey> publicKey = loadPublicKey(keyId);
		PrivateKey privateKey = (PrivateKey) loadKey(keyId
				+ Config.ACCOUNT_ALIAS_PRIVATE, password);
		if (!publicKey.isPresent() || privateKey == null)
			throw new KeyException("Key with id \"" + keyId
					+ "\" does not exist");

		return new KeyPair(publicKey.get(), privateKey);
	}

	public static Optional<PublicKey> loadPublicKey(String keyId) {
		return Optional.ofNullable((PublicKey) loadKey(keyId
				+ Config.ACCOUNT_ALIAS_PUBLIC, Config.getPublicKeyPassword()));
	}

	private static Key loadKey(String keyId, char[] password) {
		Key key;
		try {
			KeyStore ks = KeyStore.getInstance(Config.getKeyStoreType(),
					Config.getProvider());
			ks.load(new FileInputStream(Config.getKeyStore()), password);
			key = ks.getKey(keyId, password);
		} catch (KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException
				| UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

		return key;
	}

	public static void storeKeyPair(String keyId, char[] password, KeyPair keys) {
		storePublicKey(keyId, keys.getPublic(), password);
		storeKey(keyId + Config.ACCOUNT_ALIAS_PRIVATE, keys.getPrivate(),
				password, getCerts(keys));
	}

	public static void storePublicKey(String keyId, PublicKey key,
			char[] password) {
		storeKey(keyId + Config.ACCOUNT_ALIAS_PUBLIC, key, password, null);
	}

	private static void storeKey(String keyId, Key key, char[] password,
			Certificate[] certs) {
		try {
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
			ks.setKeyEntry(keyId, key, password, certs);

			ks.store(new FileOutputStream(keyStoreFile), password);
		} catch (KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private static Certificate[] getCerts(KeyPair keys) {
		/*
		 * We need a stupid certificate to store the key, so just create a
		 * self-signed one.
		 */
		Date startDate = Date.valueOf("2000-01-01");
		Date expiryDate = Date.valueOf("3000-01-01");
		BigInteger serialNumber = new BigInteger("42");

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

		X509Certificate cert;
		try {
			cert = certGen.generate(keys.getPrivate(), "BC");
		} catch (InvalidKeyException | IllegalStateException
				| NoSuchProviderException | SignatureException
				| CertificateEncodingException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		return new Certificate[] { cert };
	}

}
