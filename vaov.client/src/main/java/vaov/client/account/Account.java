package vaov.client.account;

import java.security.PublicKey;

import vaov.client.util.HashComputer;

public class Account {

	private PublicKey publicKey;

	private String hash;

	public Account(PublicKey publicKey) {
		this(HashComputer.computeHash(publicKey), publicKey);
	}

	public Account(String hash, PublicKey publicKey) {
		this.hash = hash;
		this.publicKey = publicKey;
	}

	public String getHash() {
		return hash;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

}
