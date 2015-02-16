package vaov.client.account.service;

import org.junit.Test;

import vaov.client.account.model.PrivateAccount;
import vaov.client.message.RsaSignatureComputer;
import vaov.remote.services.KeyId;

public class RsaAccountCreationServiceTest {

	@Test
	public void testCreateNewAccount() throws Exception {
		PrivateAccount privateAccount = new PrivateAccount(new KeyId("foo"),
			new RsaAccountCreationService().generateKeyPair());
		System.out.println(new RsaSignatureComputer().computeSignature("hallowelt", privateAccount.getPrivateKey()));
	}
}
