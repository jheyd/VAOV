package vaov.client.account;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

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
		keys = KeystoreService.loadKeyPair(keyId, password);
		init(keys.getPublic());
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
		KeystoreService.storeKeyPair(keyId, password, keys);
	}
}
