package vaov.remote.account.to;

import vaov.remote.ValueObject;

public class AccountTO extends ValueObject {

	private String hash;
	private PublicKeyTO publicKey;

	public AccountTO(PublicKeyTO publicKey, String hash) {
		this.publicKey = publicKey;
		this.hash = hash;
	}

	public String getHash() {
		return hash;
	}

	public PublicKeyTO getPublicKey() {
		return publicKey;
	}

}
