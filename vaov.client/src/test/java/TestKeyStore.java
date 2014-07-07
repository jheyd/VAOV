import static org.junit.Assert.*;

import org.junit.Test;

import vaov.client.vote.Helper;
import vaov.client.vote.KeyException;
import vaov.client.vote.PrivateAccount;

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
