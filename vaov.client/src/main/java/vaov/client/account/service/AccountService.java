package vaov.client.account.service;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Optional;

import vaov.client.account.model.Account;
import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import vaov.client.account.model.PublicAccount;
import vaov.client.service.ServiceFactory;
import vaov.client.util.Config;
import vaov.remote.account.to.AccountTO;
import vaov.remote.account.to.PublicKeyTO;
import vaov.remote.services.KeyId;
import vaov.remote.services.VaovAccountService;

public class AccountService {

	private KeystoreService keystoreService;
	private VaovAccountService accountService;
	private AccountCreationService accountCreationService;

	public AccountService() {
		this(new KeystoreService(), ServiceFactory.getAccountService(), new RsaAccountCreationService());
	}

	public AccountService(KeystoreService keystoreService, VaovAccountService accountService,
		AccountCreationService accountCreationService) {
		super();
		this.keystoreService = keystoreService;
		this.accountService = accountService;
		this.accountCreationService = accountCreationService;
	}

	/**
	 * Generate a new PrivateAccount and store it in the KeyStore
	 */
	public PrivateAccount createNewAccount(Password pass) {
		PrivateAccount account = accountCreationService.createAccount();
		keystoreService.storeKeyPair(account.getKeyId(), pass, account.getKeyPair());
		pass.overwrite();
		return account;
	}

	public Optional<Account> getAccount(KeyId keyId) {
		if (!hasKey(keyId)) {
			getAccountFromServer(keyId);
		}
		Optional<PublicKey> publicKey = keystoreService.loadPublicKey(keyId);

		return publicKey.map(x -> new PublicAccount(keyId, x));
	}

	public Optional<PrivateAccount> getPrivateAccount(KeyId keyId, Password password) {
		Optional<KeyPair> optional = keystoreService.loadKeyPair(keyId, password);
		password.overwrite();
		return optional.map(x -> new PrivateAccount(keyId, x));
	}

	private void getAccountFromServer(KeyId keyId) {
		AccountTO accountTO = accountService.getAccount(keyId);
		if (!keyId.equals(new KeyId(accountTO.getHash()))) {
			throw new RuntimeException("Hash from server does not match");
		}
		PublicKeyTO publicKeyTO = accountTO.getPublicKey();
		PublicKey publicKey = Config.getPublicKeyConverter().readPublicKey(publicKeyTO);
		keystoreService.storePublicKey(keyId, publicKey);
	}

	/**
	 * checks if the database has a valid entry for the hash
	 */
	private boolean hasKey(KeyId keyId) {
		return keystoreService.loadPublicKey(keyId).isPresent();
	}

}
