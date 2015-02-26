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

	public RsaAccountCreationService() {
		this(new RsaHashComputer());
	}

	public RsaAccountCreationService(HashComputer hashComputer) {
		this.hashComputer = hashComputer;
	}

	private static KeyPair generateKeyPair() {
		return getKeyPairGenerator().generateKeyPair();
	}

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

	@Override
	public PrivateAccount createAccount() {
		KeyPair keyPair = generateKeyPair();
		KeyId keyId = generateKeyId(keyPair);
		return new PrivateAccount(keyId, keyPair);
	}

	private KeyId generateKeyId(KeyPair keyPair) {
		return new KeyId(hashComputer.computeHash(keyPair.getPublic()));
	}

}
