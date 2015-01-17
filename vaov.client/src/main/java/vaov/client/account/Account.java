package vaov.client.account;

import java.security.PublicKey;

import vaov.client.util.HashComputer;
import vaov.client.util.Helper;
import vaov.client.util.KeyException;

/**
 * Every user has an account. These accounts are represented by this class. The
 * account of the user, who is running the client, has a special account, a
 * {@link PrivateAccount}.
 * 
 * Account stores the public key and the hash of the key.
 * 
 * @author arne
 * 
 */
public class Account {

	/** public key of the account */
	protected PublicKey publicKey;

	/** hash of the account */
	protected String hash;

	/**
	 * Only for PrivateAccount construction. {@link #init(PublicKey)} must be
	 * called immediatly after the key has been constructed.
	 */
	protected Account() {
	}

	/**
	 * performs a lookup in the official list and retrieves corresponding key
	 * 
	 * @param hash
	 */
	public Account(String hash) throws KeyException {
		this.hash = hash;
		PublishedAccounts pa = new PublishedAccounts();
		publicKey = pa.getKey(hash);
		// check if this is the correct public key
		Helper.verifyKey(publicKey, hash);
	}

	public Account(String hash, PublicKey publicKey) {
		this.hash = hash;
		this.publicKey = publicKey;
	}

	/**
	 * returns hash of the public key of this account.
	 * 
	 * @return
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * returns the public key of this account.
	 * 
	 * @return the public key
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * Only for PrivateAccount initialization. Stores the public key and
	 * computes the hash.
	 * 
	 * @param pk
	 *            the public key of the account.
	 * @throws KeyException
	 */
	protected void init(PublicKey pk) throws KeyException {
		publicKey = pk;
		hash = HashComputer.computeHash(publicKey);
	}
}
