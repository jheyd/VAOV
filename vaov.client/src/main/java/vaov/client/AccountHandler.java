package vaov.client;

import vaov.client.account.Account;
import vaov.client.account.PrivateAccount;
import vaov.client.util.IllegalFormatException;
import vaov.client.util.KeyException;
import vaov.client.util.VerificationException;

public abstract class AccountHandler {

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
	public static PrivateAccount createNewAccount(String keyID, char[] pass)
			throws KeyException {
		PrivateAccount acc = new PrivateAccount();
		acc.store(keyID, pass);
		for (int i = 0; i < pass.length; i++)
			pass[i] = 'a';
		return acc;
	}

	public static PrivateAccount getAccount(String keyId, char[] pass)
			throws KeyException {
		PrivateAccount pa = new PrivateAccount(keyId, pass);
		Util.overwriteChar(pass);
		return pa;
	}

	/**
	 * Register an already existing account
	 *
	 * @param keyID
	 * @param pass
	 * @return
	 */
	public static PrivateAccount registerAccount(String keyID, char[] pass,
			char[] privateKey, char[] publicKey) {
		// TODO
		try {
			PrivateAccount acc = new PrivateAccount();
			// acc = new PrivateAccount(privateKey,publicKey);
			acc.store(keyID, pass);
			Util.overwriteChar(pass);
			Util.overwriteChar(privateKey);
			return acc;
		} catch (KeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * publishes the public key, i.e. registers the Account.
	 * 
	 * @throws VerificationException
	 * @throws KeyException
	 * @throws IllegalFormatException
	 */
	public void publish(Account account) throws IllegalFormatException,
			KeyException, VerificationException {
		new MessageHandler().sendNewAccount(account);
	}

}
