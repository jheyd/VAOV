import java.io.IOException;

import org.junit.Test;

import vaov.client.vote.KeyException;
import vaov.client.vote.PrivateAccount;

@Deprecated
public class TestCreate {

	@Test
	public void test() throws KeyException, IOException {
		PrivateAccount pa = new PrivateAccount();
		pa.publish();
	}

}
