package vaov.client;

import vaov.client.account.PrivateAccount;
import vaov.client.util.KeyException;
import de.janheyd.javalibs.ask.AskUtils;

public class Util {

	/**
	 * Get PrivateAccount associated with a username form the KeyStore, asking
	 * for the password on the command line.
	 *
	 * @param alias
	 * @return
	 * @throws KeyException
	 */
	public static PrivateAccount askAcc(String alias) throws KeyException {
		Password password = Util.askPassword("password for " + alias + ": ");
		PrivateAccount acc = Control.getAccount(alias, password);
		password.overwrite();
		return acc;
	}

	public static Password askPassword(String question) {
		char[] buf = new char[256];
		AskUtils.askCharArray(question, buf);
		return new Password(buf);
	}

	/**
	 * overwrites a char Array with zeroes
	 *
	 * @param c
	 *            the Array to overwrite
	 */
	public static void overwriteCharArray(char[] c) {
		for (int i = 0; i < c.length; i++ ) {
			c[i] = 0;
		}
	}

}
