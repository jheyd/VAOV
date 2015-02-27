package vaov.client.account.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Test;

import vaov.client.account.model.PrivateAccount;
import vaov.client.util.HashComputer;

public class RsaAccountCreationServiceTest {

	private static final String HASH = "hash";
	private static final PublicKey PUBLIC_KEY = mock(PublicKey.class);
	private static final PrivateKey PRIVATE_KEY = mock(PrivateKey.class);
	private static final KeyPair KEY_PAIR = new KeyPair(PUBLIC_KEY, PRIVATE_KEY);

	private HashComputer hashComputer = mock(HashComputer.class);
	private KeyPairGenerator keyPairGenerator = mock(KeyPairGenerator.class);
	private RsaAccountCreationService rsaAccountCreationService = new RsaAccountCreationService(hashComputer,
		keyPairGenerator);

	@Test
	public void testCreateNewAccount() throws Exception {
		when(keyPairGenerator.generateKeyPair()).thenReturn(KEY_PAIR);
		when(hashComputer.computeHash(PUBLIC_KEY)).thenReturn(HASH);

		PrivateAccount privateAccount = rsaAccountCreationService.createAccount();

		assertThat(privateAccount.getPrivateKey(), is(PRIVATE_KEY));
		assertThat(privateAccount.getPublicKey(), is(PUBLIC_KEY));
		assertThat(privateAccount.getKeyId().getAlias(), is(HASH));
	}
}
