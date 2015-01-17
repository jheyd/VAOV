package vaov.client.account;

import java.security.KeyPair;
import java.security.PrivateKey;

import vaov.client.util.KeyException;
import vaov.remote.services.KeyId;

public class PrivateAccount extends Account {

	PrivateKey privateKey;

	public PrivateAccount(KeyId keyId, KeyPair keyPair) throws KeyException {
		super(keyId, keyPair.getPublic());
		this.privateKey = keyPair.getPrivate();
	}

	public KeyPair getKeyPair() {
		return new KeyPair(getPublicKey(), getPrivateKey());
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public String getAlias() {
		return getKeyId().getAlias();
	}

}
