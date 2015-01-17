package vaov.client.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V1CertificateGenerator;

import vaov.client.message.Message;
import vaov.client.util.Config;
import vaov.client.util.KeyException;

public class PrivateAccount extends Account {

	KeyPair keys;

	/**
	 * creates a new private Account.
	 */
	public PrivateAccount() throws KeyException {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		kpg.initialize(4096);
		keys = kpg.generateKeyPair();
		init(keys.getPublic());
	}

	/**
	 * Loads a previously created PrivateAccount from the KeyStore.
	 *
	 * @param password
	 * @throws KeyException
	 */
	public PrivateAccount(String keyId, char[] password) throws KeyException {
		try {
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

			keys = new KeyPair(publicKey, privateKey);
			init(keys.getPublic());
		} catch (NoSuchAlgorithmException | CertificateException
				| KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new KeyException("Cannot open KeyStore", e);
		} catch (UnrecoverableKeyException e) {
			throw new KeyException("Cannot read Key from KeyStore", e);
		}
	}

	/**
	 * Gets the private key of this account. Usually used to sign messages.
	 *
	 * @see Message#send()
	 * @return the private key
	 */
	public PrivateKey getPrivateKey() {
		return keys.getPrivate();
	}

	/**
	 * stores the keys of this account.
	 *
	 * @param password
	 * @throws KeyException
	 */
	public void store(String keyId, char[] password) throws KeyException {
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
			ks.setKeyEntry(keyId + Config.ACCOUNT_ALIAS_PUBLIC,
					keys.getPublic(), password, null);

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

			ks.setKeyEntry(keyId + Config.ACCOUNT_ALIAS_PRIVATE,
					keys.getPrivate(), password, certs);
			ks.store(new FileOutputStream(keyStoreFile), password);
		} catch (NoSuchAlgorithmException | CertificateException
				| KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new KeyException("Cannot write KeyStore to store the key", e);
		}
	}
}
