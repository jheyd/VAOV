package de.piratenpartei.id.frontend;

import de.piratenpartei.id.vote.PrivateAccount;

public class Model extends Client {
	private PrivateAccount account;

	public PrivateAccount getAccount() {
		return account;
	}

	public void setAccount(PrivateAccount account) {
		this.account = account;
	}
	
	public void loadAccount(String username, char[] pass){
		this.account = this.getAcc(username, pass);
	}

}
