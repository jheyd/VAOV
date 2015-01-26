package vaov.client.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import vaov.remote.services.KeyId;

public class AccountCreationService {

	private static final String ALGORITHM = "RSA"; //$NON-NLS-1$
	private static final int KEY_SIZE = 4096;

	public static KeyId generateKeyId(KeyPair keyPair) {
		return new KeyId(HashComputer.computeHash(keyPair.getPublic()));
	}

	public static KeyPair generateKeyPair() {
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

}
