import org.junit.Test;

import vaov.client.reduced.client.MessageHandler;
import vaov.client.reduced.client.writers.MessageWriterDebugImpl;
import vaov.client.vote.PrivateAccount;

@Deprecated
public class TestMessenger {

	@Test
	public void test() {
		PrivateAccount pa;
		try {
			pa = new PrivateAccount();
			new MessageHandler(new MessageWriterDebugImpl()).sendMessageToUser(
					"troll", "Hallo, ich kenn dich nicht", pa);
			System.out.println("successful");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}
	}

}
