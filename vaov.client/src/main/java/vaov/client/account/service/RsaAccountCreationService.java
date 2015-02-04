package vaov.client.account.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RsaAccountCreationService implements AccountCreationService {

	private static final String ALGORITHM = "RSA"; //$NON-NLS-1$
	private static final int KEY_SIZE = 4096;

	@Override
	public KeyPair generateKeyPair() {
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
