package vaov.client.account.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Optional;

import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import vaov.client.service.ServiceFactory;
import vaov.client.util.Config;
import vaov.client.util.HashComputer;
import vaov.remote.account.to.AccountTO;
import vaov.remote.account.to.PublicKeyTO;
import vaov.remote.services.KeyId;
import vaov.remote.services.VaovAccountService;

public abstract class AccountService {

	private static final String ALGORITHM = "RSA"; //$NON-NLS-1$
	private static final int KEY_SIZE = 4096;

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
		KeyPair keyPair = AccountService.generateKeyPair();
		KeyId keyId = AccountService.generateKeyId(keyPair);
		return new PrivateAccount(keyId, keyPair);
	}

	public static KeyId generateKeyId(KeyPair keyPair) {
		return new KeyId(HashComputer.computeHash(keyPair.getPublic()));
	}

	public static KeyPair generateKeyPair() {
		return getKeyPairGenerator().generateKeyPair();
	}

	static KeyPairGenerator getKeyPairGenerator() {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		kpg.initialize(KEY_SIZE);
		return kpg;
	}

	public static Optional<PublicKey> getKey(KeyId keyId) {
		if (!hasKey(keyId)) {
			getAccountFromServer(keyId);
		}
		return KeystoreService.loadPublicKey(keyId);
	}

	private static void getAccountFromServer(KeyId keyId) {
		VaovAccountService accountService = ServiceFactory.getAccountService();
		AccountTO accountTO = accountService.getAccount(keyId);
		if (!keyId.equals(new KeyId(accountTO.getHash()))) {
			throw new RuntimeException("Hash from server does not match");
		}
		PublicKeyTO publicKeyTO = accountTO.getPublicKey();
		PublicKey publicKey = Config.getPublicKeyConverter().readPublicKey(publicKeyTO);
		KeystoreService.storePublicKey(keyId, publicKey);
	}

	/**
	 * checks if the database has a valid entry for the hash
	 */
	static boolean hasKey(KeyId keyId) {
		return KeystoreService.loadPublicKey(keyId).isPresent();
	}

}
