package vaov.client.util;

import java.util.Optional;

import vaov.client.Control;
import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import de.janheyd.javalibs.ask.AskUtils;

public class Util {

	/**
	 * Get PrivateAccount associated with a username form the KeyStore, asking
	 * for the password on the command line.
	 *
	 * @param alias
	 * @return
	 *         @
	 */
	public static Optional<PrivateAccount> askAcc(String alias) {
		Password password = Util.askPassword("password for " + alias + ": ");
		Optional<PrivateAccount> acc = new Control().getAccount(alias, password);
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
