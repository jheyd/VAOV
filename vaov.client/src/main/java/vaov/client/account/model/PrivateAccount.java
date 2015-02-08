package vaov.client.account.model;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import vaov.remote.services.KeyId;

public class PrivateAccount extends Account {

	private KeyPair keyPair;

	public PrivateAccount(KeyId keyId, KeyPair keyPair) {
		super(keyId);
		this.keyPair = keyPair;
	}

	public String getAlias() {
		return getKeyId().getAlias();
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}

	@Override
	public PublicKey getPublicKey() {
		return keyPair.getPublic();
	}

}
