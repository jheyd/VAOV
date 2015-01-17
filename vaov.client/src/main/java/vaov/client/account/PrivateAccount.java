package vaov.client.account;

import java.security.KeyPair;
import java.security.PrivateKey;

import vaov.client.util.KeyException;

public class PrivateAccount extends Account {

	PrivateKey privateKey;

	public PrivateAccount(KeyPair keyPair) throws KeyException {
		super(keyPair.getPublic());
		this.privateKey = keyPair.getPrivate();
	}

	public KeyPair getKeyPair() {
		return new KeyPair(getPublicKey(), getPrivateKey());
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

}
