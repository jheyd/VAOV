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

import vaov.client.Password;
import vaov.remote.services.KeyId;

public class KeystoreService {

	public static KeyPair loadKeyPair(KeyId keyId, Password password) {
		Optional<PublicKey> publicKey = Optional.ofNullable((PublicKey) loadKey(keyId.getPublicAlias(), password,
		Config.getKeyStore()));
		PrivateKey privateKey = (PrivateKey) loadKey(keyId.getPrivateAlias(), password, Config.getKeyStore());
		if (!publicKey.isPresent() || privateKey == null) {
			throw new KeyException("Key with id \"" + keyId + "\" does not exist");
		}

		return new KeyPair(publicKey.get(), privateKey);
	}

	public static Optional<PublicKey> loadPublicKey(KeyId keyId) {
		return Optional.ofNullable((PublicKey) loadKey(keyId.getPublicAlias(), Config.getPublicKeyPassword(),
		Config.getPublicKeysFile()));
	}

	public static void storeKeyPair(KeyId keyId, Password password, KeyPair keys) {
		storeKey(keyId.getPublicAlias(), keys.getPublic(), password, Config.getKeyStore(), null);
		storeKey(keyId.getPrivateAlias(), keys.getPrivate(), password, Config.getKeyStore(), getCerts(keys));
	}

	public static void storePublicKey(KeyId keyId, PublicKey key) {
		storeKey(keyId.getPublicAlias(), key, Config.getPublicKeyPassword(), Config.getPublicKeysFile(), null);
	}

	private static Certificate[] getCerts(KeyPair keys) {
		/* We need a stupid certificate to store the key, so just create a
		 * self-signed one. */
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
		} catch (InvalidKeyException | IllegalStateException | NoSuchProviderException | SignatureException
		| CertificateEncodingException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		return new Certificate[] { cert };
	}

	private static Key loadKey(String alias, Password password, File keyStoreFile) {
		Key key;
		try (FileInputStream fileInputStream = new FileInputStream(keyStoreFile)) {
			KeyStore ks = KeyStore.getInstance(Config.getKeyStoreType(), Config.getProvider());
			char[] passwordChars = password.getCharArray();
			ks.load(fileInputStream, passwordChars);
			key = ks.getKey(alias, passwordChars);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
		| UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

		return key;
	}

	private static void storeKey(String alias, Key key, Password password, File keyStoreFile, Certificate[] certs) {
		try {
			KeyStore ks = KeyStore.getInstance(Config.getKeyStoreType(), Config.getProvider());
			char[] passwordChars = password.getCharArray();
			if (keyStoreFile.exists()) {
				if (keyStoreFile.isFile()) {
					try (FileInputStream fileInputStream = new FileInputStream(Config.getKeyStore());) {
						ks.load(fileInputStream, passwordChars);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new RuntimeException("a directory with the name of the keystore exists");
				}
			} else {
				ks.load(null, null);
				keyStoreFile.getParentFile().mkdirs();
				keyStoreFile.createNewFile();
			}
			ks.setKeyEntry(alias, key, passwordChars, certs);

			try (FileOutputStream fileOutputStream = new FileOutputStream(Config.getKeyStore());) {
				ks.store(fileOutputStream, passwordChars);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
