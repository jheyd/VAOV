package vaov.client.account;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import vaov.client.message.Message;
import vaov.client.util.KeyException;
import vaov.client.util.KeystoreService;

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
			keys = KeystoreService.loadKeyPair(keyId, password);
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
			KeystoreService.storeKey(keyId, password, keys);
		} catch (NoSuchAlgorithmException | CertificateException
				| KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new KeyException("Cannot write KeyStore to store the key", e);
		}
	}
}
