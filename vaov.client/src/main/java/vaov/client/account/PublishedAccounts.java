package vaov.client.account;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import vaov.client.util.IllegalFormatException;
import vaov.client.util.KeyException;

/**
 * This class manages the list of published accounts.
 * 
 * Currently this list is loaded statically. This should be changed in the
 * future.
 * 
 * @author arne
 * 
 */
public class PublishedAccounts {
	private Map<String, Account> accounts;

	private static final PublishedAccounts INSTANCE = createPublishedAccounts();

	private static PublishedAccounts createPublishedAccounts() {
		return new PublishedAccounts();
	}

	public static PublishedAccounts getInstance() {
		return INSTANCE;
	}

	/**
	 * Loads list of published accounts from a file (possibly fetched from the
	 * internet). The file has to have entries of the form: <br>
	 * Hash: <hash of RSA key 1> <br>
	 * Modulus: <modulus of the RSA key 1> <br>
	 * Exponent: <public exponent of the RSA key 1> <br>
	 * Hash: <hash of RSA key 2> <br>
	 * Modulus: <modulus of the RSA key 2> <br>
	 * Exponent: <public exponent of the RSA key 2> <br>
	 * Extra lines are not allowed
	 * 
	 * @throws IllegalFormatException
	 */
	private PublishedAccounts() {
		accounts = new HashMap<>();
	}

	/**
	 * Loads Key from Database
	 * 
	 * @param hash
	 * @return
	 * @throws KeyException
	 */
	public PublicKey getKey(String hash) throws KeyException {
		if (!hasKey(hash)) {
			// TODO jan 17.01.2015 replace new Account()
			// with WebService call
			accounts.put(hash, new Account());
		}
		return accounts.get(hash).getPublicKey();
	}

	/**
	 * checks if the database has a valid entry for the hash
	 * 
	 * @param hash
	 * @return
	 */
	public boolean hasKey(String hash) {
		return accounts.containsKey(hash);
	}
}
