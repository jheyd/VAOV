package vaov.client.account.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import vaov.client.account.model.PrivateAccount;
import vaov.client.util.HashComputer;
import vaov.client.util.RsaHashComputer;
import vaov.remote.services.KeyId;

public class RsaAccountCreationService implements AccountCreationService {

	private static final String ALGORITHM = "RSA";
	private static final int KEY_SIZE = 4096;
	private HashComputer hashComputer;
	private KeyPairGenerator keyPairGenerator;

	private static KeyPairGenerator getKeyPairGenerator() {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		kpg.initialize(KEY_SIZE);
		return kpg;
	}

	public RsaAccountCreationService() {
		this(new RsaHashComputer(), getKeyPairGenerator());
	}

	public RsaAccountCreationService(HashComputer hashComputer, KeyPairGenerator keyPairGenerator) {
		this.hashComputer = hashComputer;
		this.keyPairGenerator = keyPairGenerator;
	}

	@Override
	public PrivateAccount createAccount() {
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		KeyId keyId = generateKeyId(keyPair);
		return new PrivateAccount(keyId, keyPair);
	}

	private KeyId generateKeyId(KeyPair keyPair) {
		return new KeyId(hashComputer.computeHash(keyPair.getPublic()));
	}

}
