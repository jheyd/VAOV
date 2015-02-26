package vaov.client.account.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.Test;

import vaov.client.account.model.PrivateAccount;
import vaov.remote.services.KeyId;

public class RsaAccountCreationServiceIntegrationTest {

	private static final KeyId KEY_ID = new KeyId("foo");

	@Test
	public void testCreateNewAccount() throws Exception {
		KeyPair keyPair = new RsaAccountCreationService().generateKeyPair();
		PrivateAccount privateAccount = new PrivateAccount(KEY_ID, keyPair);

		assertThat(privateAccount.getPrivateKey() instanceof RSAPrivateKey, is(true));
		assertThat(privateAccount.getPublicKey() instanceof RSAPublicKey, is(true));
	}
}
