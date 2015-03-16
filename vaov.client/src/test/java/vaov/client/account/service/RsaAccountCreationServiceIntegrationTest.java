package vaov.client.account.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.Test;

import vaov.client.account.model.PrivateAccount;

public class RsaAccountCreationServiceIntegrationTest {

	@Test
	public void testCreateNewAccount() throws Exception {
		PrivateAccount privateAccount = RsaAccountCreationService.createRsaAccountCreationService().createAccount();

		assertThat(privateAccount.getPrivateKey() instanceof RSAPrivateKey, is(true));
		assertThat(privateAccount.getPublicKey() instanceof RSAPublicKey, is(true));
	}
}
