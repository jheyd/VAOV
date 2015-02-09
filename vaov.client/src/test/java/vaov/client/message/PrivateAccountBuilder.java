package vaov.client.message;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import vaov.client.account.model.PrivateAccount;
import vaov.remote.services.KeyId;

public class PrivateAccountBuilder {

	private KeyId keyId = new KeyId("0");
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public PrivateAccount create() {
		return new PrivateAccount(keyId, new KeyPair(publicKey, privateKey));
	}

	public PrivateAccountBuilder withPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
		return this;
	}

	public PrivateAccountBuilder withPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
		return this;
	}

}
