import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import vaov.client.account.Account;
import vaov.client.message.Message;
import vaov.client.util.IllegalFormatException;
import vaov.client.util.KeyException;
import vaov.client.util.VerificationException;

public class TestVerify {

	@Test
	public void testWrongKey() {
		try {
			new Account(
					"ic_ZTBgESfPpRL6EnoOVEJNN5a4PvFNotQiIJAbZf7PxG8eseIn5nwshybMmT6W9ykIWzxQaOml7L2Q4VkwXPA==");
			fail("must not succed, since key does not fit to hash!");
		} catch (KeyException ex) {
			assertTrue(true);
		}
	}

	@Test
	public void testCorrectKey() throws KeyException {
		Account a = new Account(
				"i9YMFc+vWTpO3B5hxwsg80UvHX6sGUKdhyly7RQOfg230UZfaVfN6fqHMtfb3bxydpRUEuQvgKgW450FwzrSww==");
		assertTrue(a.isPublished());
		assertFalse(a.isVerified());
	}

	@Test
	@Ignore
	// TODO jan 17.01.2015 Beispielfile erstellen
	public void testMessage() throws KeyException, IllegalFormatException,
			IOException, VerificationException {
		new Message(new FileInputStream("test_files/message"));
	}
}
