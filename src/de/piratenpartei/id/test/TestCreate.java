package de.piratenpartei.id.test;

import java.io.IOException;

import org.junit.Test;

import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

public class TestCreate {

	@Test
	public void test() throws KeyException, IOException {
		PrivateAccount pa = new PrivateAccount();
		pa.publish();
	}

}
