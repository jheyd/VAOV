package vaov.remote.account.to;

public class AccountTO {

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
