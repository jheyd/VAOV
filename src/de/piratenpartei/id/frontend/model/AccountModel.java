package de.piratenpartei.id.frontend.model;

import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

public class AccountModel {
	private PrivateAccount account;

	public PrivateAccount getAccount() {
		return account;
	}

	public void setAccount(PrivateAccount account) {
		this.account = account;
	}
	
	public void loadAccount(String username, char[] pass) throws KeyException{
		this.account = new PrivateAccount(username, pass);
	}

}
