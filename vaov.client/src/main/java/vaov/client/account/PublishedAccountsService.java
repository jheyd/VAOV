package vaov.client.account;

import java.security.PublicKey;

import vaov.client.service.ServiceFactory;
import vaov.client.util.Config;
import vaov.client.util.KeyException;
import vaov.client.util.KeystoreService;
import vaov.client.util.PublicKeyConverter;
import vaov.remote.account.to.AccountTO;
import vaov.remote.account.to.PublicKeyTO;
import vaov.remote.services.KeyId;
import vaov.remote.services.VaovAccountService;

public abstract class PublishedAccountsService {

	/**
	 * Loads Key from Database
	 */
	public static PublicKey getKey(KeyId keyId) throws KeyException {
		if (!hasKey(keyId)) {
			getAccountFromServer(keyId);
		}
		return KeystoreService.loadKeyPair(keyId, Config.getPublicKeyPassword()).getPublic();
	}

	private static void getAccountFromServer(KeyId keyId) throws KeyException {
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
	 */
	private static boolean hasKey(KeyId keyId) {
		return KeystoreService.loadPublicKey(keyId).isPresent();
	}
}
