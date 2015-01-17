package vaov.client.account;

import java.security.PublicKey;

import vaov.remote.services.KeyId;

public class Account {

	private PublicKey publicKey;

	private KeyId keyId;

	public Account(KeyId keyId, PublicKey publicKey) {
		this.keyId = keyId;
		this.publicKey = publicKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public KeyId getKeyId() {
		return keyId;
	}
}
