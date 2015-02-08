package vaov.client.account.model;

import java.security.PublicKey;

import vaov.remote.services.KeyId;

public class PublicAccount extends Account {

	private PublicKey publicKey;

	public PublicAccount(KeyId keyId, PublicKey publicKey) {
		super(keyId);
		this.publicKey = publicKey;
	}

	@Override
	public PublicKey getPublicKey() {
		return publicKey;
	}
}
