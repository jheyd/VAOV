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
	public static PrivateAccount createNewAccount(char[] pass) {
		PrivateAccount account = getNewPrivateAccount();
		KeystoreService.storeKeyPair(account.getKeyId(), pass, account.getKeyPair());
		Util.overwriteCharArray(pass);
		return account;
	}

	private static PrivateAccount getNewPrivateAccount() {
		KeyPair keyPair = AccountCreationService.generateKeyPair();
		KeyId keyId = AccountCreationService.generateKeyId(keyPair);
		return new PrivateAccount(keyId, keyPair);
	}

	public static PrivateAccount getAccount(KeyId keyId, char[] pass) {
		PrivateAccount account = new PrivateAccount(keyId, KeystoreService.loadKeyPair(keyId, pass));
		Util.overwriteCharArray(pass);
		return account;
	}

}
