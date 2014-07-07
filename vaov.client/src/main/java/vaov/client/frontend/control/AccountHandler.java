package vaov.client.frontend.control;

import vaov.client.frontend.model.AccountModel;
import vaov.client.vote.KeyException;
import vaov.client.vote.PrivateAccount;

public class AccountHandler {

	AccountModel model;

	public AccountHandler() {
		model = new AccountModel();
	}

	/**
	 * Generate a new Account and store it in the KeyStore
	 * 
	 * @param keyID
	 *            Username of the new Account
	 * @param pass
	 *            Password of the new Account
	 * @return the new Account
	 * @throws KeyException
	 */
	public PrivateAccount createNewAccount(String keyID, char[] pass)
			throws KeyException {
		PrivateAccount acc = new PrivateAccount();
		acc.store(keyID, pass);
		for (int i = 0; i < pass.length; i++)
			pass[i] = 'a';
		return acc;
	}

	public PrivateAccount getAccount(char[] pass) throws KeyException {
		return model.getPrivateAccount(pass);
	}

	/**
	 * Register an already existing account
	 * 
	 * @param keyID
	 * @param pass
	 * @return
	 */
	public PrivateAccount registerAccount(String keyID, char[] pass,
			char[] privateKey, char[] publicKey) {
		// TODO
		try {
			PrivateAccount acc = new PrivateAccount();
			// acc = new PrivateAccount(privateKey,publicKey);
			acc.store(keyID, pass);
			for (int i = 0; i < pass.length; i++)
				pass[i] = 'a';
			for (int i = 0; i < privateKey.length; i++)
				privateKey[i] = 'a';
			return acc;
		} catch (KeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void registerNewAccount(String keyID, char[] pass)
			throws KeyException {
		createNewAccount(keyID, pass);
		this.model.setPrivateAccount(keyID);
	}

	public void setAccount(String keyID) {
		model.setPrivateAccount(keyID);
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}
}
