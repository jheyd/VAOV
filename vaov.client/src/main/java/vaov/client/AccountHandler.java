package vaov.client;

import vaov.client.account.PrivateAccount;
import vaov.client.util.Helper;
import vaov.client.util.KeyException;
import vaov.client.util.KeystoreService;

public abstract class AccountHandler {

	/**
	 * Generate a new Account and store it in the KeyStore
	 */
	public static PrivateAccount createNewAccount(String keyID, char[] pass) {
		PrivateAccount account = getNewPrivateAccount();
		KeystoreService.storeKeyPair(keyID, pass, account.getKeyPair());
		Util.overwriteCharArray(pass);
		return account;
	}

	private static PrivateAccount getNewPrivateAccount() {
		return new PrivateAccount(Helper.generateKeyPair());
	}

	public static PrivateAccount getAccount(String keyId, char[] pass)
			throws KeyException {
		PrivateAccount account = new PrivateAccount(
				KeystoreService.loadKeyPair(keyId, pass));
		Util.overwriteCharArray(pass);
		return account;
	}

}
