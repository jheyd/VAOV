import static org.junit.Assert.assertFalse;

import org.junit.Test;

import vaov.client.account.PrivateAccount;
import vaov.client.util.KeyException;
import vaov.client.util.KeystoreService;

public class TestKeyStore {

	@Test
	public void storeAccount() throws KeyException {
		KeystoreService.initKeyStore("foobar".toCharArray());
		PrivateAccount pa = new PrivateAccount();
		pa.store("testAccount", "foobar".toCharArray());
	}

	@Test
	public void loadAccount() throws KeyException {
		KeystoreService.initKeyStore("foobar".toCharArray());
		PrivateAccount pa = new PrivateAccount();
		pa.store("testAccount", "foobar".toCharArray());
		new PrivateAccount("testAccount", "foobar".toCharArray());
	}

	@Test
	public void loadWrongAccount() {
		try {
			PrivateAccount pa = new PrivateAccount("testWrongAccount",
					"foobar".toCharArray());
			assertFalse("Must throw exception!", true);
		} catch (KeyException e) {
			// must be thrown
		}
	}
}
