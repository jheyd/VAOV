package vaov.client.account.model;

import java.security.PublicKey;

import vaov.remote.services.KeyId;

public abstract class Account {

	private KeyId keyId;

	public Account(KeyId keyId) {
		this.keyId = keyId;
	}

	public KeyId getKeyId() {
		return keyId;
	}

	public abstract PublicKey getPublicKey();

}