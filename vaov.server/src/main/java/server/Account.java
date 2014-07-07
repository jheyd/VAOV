package de.piratenpartei.id.server;

import java.security.PublicKey;

public class Account {
	private PublicKey pk;
	private String username;
	
	public Account(String username, String publicExp, String modulus) {
		this.setUsername(username);
		// TODO
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public PublicKey getPk() {
		return pk;
	}
	
}
