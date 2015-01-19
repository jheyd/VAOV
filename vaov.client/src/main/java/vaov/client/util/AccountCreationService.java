package vaov.client.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import vaov.remote.services.KeyId;

public class AccountCreationService {

	public static KeyPair generateKeyPair() {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		kpg.initialize(4096);
		KeyPair generateKeyPair = kpg.generateKeyPair();
		return generateKeyPair;
	}

	public static KeyId generateKeyId(KeyPair keyPair) {
		return new KeyId(HashComputer.computeHash(keyPair.getPublic()));
	}

}
