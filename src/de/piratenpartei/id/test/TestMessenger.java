package de.piratenpartei.id.test;

import de.piratenpartei.id.vote.Messenger;
import de.piratenpartei.id.vote.PrivateAccount;

import org.junit.Test;

public class TestMessenger {

	@Test
	public void test() {
		PrivateAccount pa;
		try {
			pa = new PrivateAccount();
			Messenger.sendMessageToUser("troll", "Hallo, ich kenn dich nicht", pa);
			System.out.println("successful");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}
	}

}
