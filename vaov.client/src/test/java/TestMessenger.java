import org.junit.Test;

import vaov.client.frontend.control.MessageHandler;
import vaov.client.vote.PrivateAccount;

@Deprecated
public class TestMessenger {

	@Test
	public void test() {
		PrivateAccount pa;
		try {
			pa = new PrivateAccount();
			MessageHandler.sendMessageToUser("troll", "Hallo, ich kenn dich nicht", pa);
			System.out.println("successful");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}
	}

}
