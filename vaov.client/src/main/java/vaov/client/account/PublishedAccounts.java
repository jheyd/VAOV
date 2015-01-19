package vaov.client.account;

import java.security.PublicKey;

import vaov.client.service.ServiceFactory;
import vaov.client.util.Config;
import vaov.client.util.IllegalFormatException;
import vaov.client.util.KeyException;
import vaov.client.util.KeystoreService;
import vaov.client.util.PublicKeyConverter;
import vaov.remote.account.to.AccountTO;
import vaov.remote.account.to.PublicKeyTO;
import vaov.remote.services.KeyId;
import vaov.remote.services.VaovAccountService;

/**
 * This class manages the list of published accounts.
 *
 * Currently this list is loaded statically. This should be changed in the
 * future.
 *
 * @author arne
 *
 */
public class PublishedAccounts {

	public PublishedAccounts() {
	}

	/**
	 * Loads Key from Database
	 *
	 * @param keyId
	 * @return
	 * @throws KeyException
	 * @throws IllegalFormatException
	 */
	public PublicKey getKey(KeyId keyId) throws KeyException {
		if (!hasKey(keyId)) {
			getAccountFromServer(keyId);
		}
		return KeystoreService.loadKeyPair(keyId, Config.getPublicKeyPassword()).getPublic();
	}

	private void getAccountFromServer(KeyId keyId) throws KeyException {
		VaovAccountService accountService = ServiceFactory.getAccountService();
		AccountTO accountTO = accountService.getAccount(keyId);
		if (!keyId.equals(accountTO.getHash())) {
			throw new KeyException("Hash from server does not match");
		}
		PublicKeyTO publicKeyTO = accountTO.getPublicKey();
		PublicKey publicKey = PublicKeyConverter.readPublicKey(publicKeyTO.getModulus(), publicKeyTO.getExponent());
		KeystoreService.storePublicKey(keyId, publicKey, Config.getPublicKeyPassword());
	}

	/**
	 * checks if the database has a valid entry for the hash
	 *
	 * @param keyId
	 * @return
	 */
	private boolean hasKey(KeyId keyId) {
		return KeystoreService.loadPublicKey(keyId).isPresent();
	}
}
