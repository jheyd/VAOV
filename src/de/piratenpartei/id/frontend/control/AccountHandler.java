package de.piratenpartei.id.frontend.control;

import de.piratenpartei.id.frontend.model.AccountModel;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

public class AccountHandler {
	
	AccountModel model;
	
	public AccountHandler() {
		model = new AccountModel();
	}
	
	public PrivateAccount getAccount() {
		return model.getAccount();
	}

	public void loadAccount(String username, char[] pass) throws KeyException {
		this.model.setAccount(new PrivateAccount(username,pass));
	}

	/**
	 * Register an already existing account
	 * @param username
	 * @param pass
	 * @return
	 */
	public PrivateAccount registerAccount(String username, char[] pass, char[] privateKey, char[] publicKey) {
		// TODO
		try {
			PrivateAccount acc = new PrivateAccount();
			// acc = new PrivateAccount(privateKey,publicKey);
			acc.store(username, pass);
			for(int i=0; i<pass.length; i++) pass[i] = 'a';
			for(int i=0; i<privateKey.length; i++) privateKey[i] = 'a';
			return acc;
		}
		catch (KeyException e) { e.printStackTrace(); }
		return null;
	}


	/**
	 * Generate a new Account and store it in the KeyStore
	 * @param username Username of the new Account
	 * @param pass Password of the new Account
	 * @return the new Account
	 * @throws KeyException 
	 */
	public PrivateAccount createNewAccount(String username, char[] pass) throws KeyException {
		PrivateAccount acc = new PrivateAccount();
		acc.store(username, pass);
		for(int i=0; i<pass.length; i++) pass[i] = 'a';
		return acc;
	}

	public void registerNewAccount(String username, char[] pass) throws KeyException {
		this.model.setAccount(createNewAccount(username, pass));
	}
	

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
}
