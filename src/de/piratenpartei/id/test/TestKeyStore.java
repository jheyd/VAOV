package de.piratenpartei.id.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.piratenpartei.id.vote.Helper;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

public class TestKeyStore {

	@Test
	public void storeAccount() throws KeyException {
		Helper.initKeyStore("foobar".toCharArray());
		PrivateAccount pa = new PrivateAccount();
		pa.store("testAccount", "foobar".toCharArray());
	}

	@Test
	public void loadAccount() throws KeyException {
		PrivateAccount pa = new PrivateAccount("testAccount", "foobar".toCharArray());
	}
	
	@Test
	public void loadWrongAccount() {
		try {
			PrivateAccount pa = new PrivateAccount("testWrongAccount", "foobar".toCharArray());
			assertFalse("Must throw exception!", true);
		} catch (KeyException e) {
			// must be thrown
		}
	}
}
