package vaov.client;

import java.security.KeyPair;

import vaov.client.account.PrivateAccount;
import vaov.client.util.AccountCreationService;
import vaov.client.util.KeystoreService;
import vaov.remote.services.KeyId;

public abstract class AccountHandler {

	/**
	 * Generate a new Account and store it in the KeyStore
	 */
	public static PrivateAccount createNewAccount(Password pass) {
		PrivateAccount account = getNewPrivateAccount();
		KeystoreService.storeKeyPair(account.getKeyId(), pass, account.getKeyPair());
		pass.overwrite();
		return account;
	}

	public static PrivateAccount getAccount(KeyId keyId, Password password) {
		PrivateAccount account = new PrivateAccount(keyId, KeystoreService.loadKeyPair(keyId, password));
		password.overwrite();
		return account;
	}

	private static PrivateAccount getNewPrivateAccount() {
		KeyPair keyPair = AccountCreationService.generateKeyPair();
		KeyId keyId = AccountCreationService.generateKeyId(keyPair);
		return new PrivateAccount(keyId, keyPair);
	}

}
