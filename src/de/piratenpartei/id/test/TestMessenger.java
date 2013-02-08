package de.piratenpartei.id.test;

import de.piratenpartei.id.frontend.control.MessageHandler;
import de.piratenpartei.id.vote.PrivateAccount;

import org.junit.Test;

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
