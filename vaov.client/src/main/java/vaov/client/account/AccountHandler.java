package vaov.client.account;

import java.security.KeyPair;
import java.util.Optional;

import vaov.client.util.KeystoreService;
import vaov.client.util.Password;
import vaov.remote.services.KeyId;

public abstract class AccountHandler {

	/**
	 * Generate a new PrivateAccount and store it in the KeyStore
	 */
	public static PrivateAccount createNewAccount(Password pass) {
		PrivateAccount account = getNewPrivateAccount();
		KeystoreService.storeKeyPair(account.getKeyId(), pass, account.getKeyPair());
		pass.overwrite();
		return account;
	}

	public static Optional<PrivateAccount> getAccount(KeyId keyId, Password password) {
		Optional<KeyPair> optional = KeystoreService.loadKeyPair(keyId, password);
		if (!optional.isPresent()) {
			return Optional.empty();
		}
		PrivateAccount account = new PrivateAccount(keyId, optional.get());
		password.overwrite();
		return Optional.of(account);
	}

	private static PrivateAccount getNewPrivateAccount() {
		KeyPair keyPair = AccountCreationService.generateKeyPair();
		KeyId keyId = AccountCreationService.generateKeyId(keyPair);
		return new PrivateAccount(keyId, keyPair);
	}

}
