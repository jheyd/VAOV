package vaov.client.account.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

import org.junit.Test;

import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import vaov.client.util.HashComputer;
import vaov.remote.services.KeyId;
import vaov.remote.services.VaovAccountService;
import de.janheyd.javalibs.test.LambdaArgumentMatcher;

public class AccountServiceTest {

	private KeyPair KEY_PAIR = new KeyPair(mock(PublicKey.class), mock(PrivateKey.class));
	private KeyId KEY_ID = new KeyId("0");

	private KeystoreService keystoreServiceMock = mock(KeystoreService.class);
	private HashComputer hashComputerMock = mock(HashComputer.class);
	private VaovAccountService vaovAccountServiceMock = mock(VaovAccountService.class);
	private AccountCreationService accountCreationServiceMock = mock(AccountCreationService.class);
	private AccountService accountService = new AccountService(keystoreServiceMock, hashComputerMock,
	vaovAccountServiceMock, accountCreationServiceMock);

	@Test
	public void testCreateNewAccount() {
		Password pass = new Password("password".toCharArray());
		String hash = "1";
		when(accountCreationServiceMock.generateKeyPair()).thenReturn(KEY_PAIR);
		when(hashComputerMock.computeHash(any(PublicKey.class))).thenReturn(hash);

		PrivateAccount createdAccount = accountService.createNewAccount(pass);

		verify(keystoreServiceMock).storeKeyPair(
		eq(createdAccount.getKeyId()),
		eq(pass),
		argThat(new LambdaArgumentMatcher<KeyPair>(kp -> {
			return kp.getPublic().equals(createdAccount.getPublicKey())
			&& kp.getPrivate().equals(createdAccount.getPrivateKey());
		})));

		assertTrue(pass.isOverwritten());
		assertThat(createdAccount.getKeyId().getAlias(), is(hash));
	}

	@Test
	public void testGetPrivateAccount() {
		Password password = new Password("password".toCharArray());
		when(keystoreServiceMock.loadKeyPair(KEY_ID, password)).thenReturn(Optional.of(KEY_PAIR));

		Optional<PrivateAccount> account = accountService.getPrivateAccount(KEY_ID, password);

		assertThat(account.isPresent(), is(true));
		assertThat(account.get().getKeyPair().getPublic(), is(equalTo(KEY_PAIR.getPublic())));
		assertThat(account.get().getKeyPair().getPrivate(), is(equalTo(KEY_PAIR.getPrivate())));
	}

	@Test
	public void testGetAccount() {
		KeyId keyId = new KeyId("1");
		when(keystoreServiceMock.loadPublicKey(keyId)).thenReturn(Optional.of(KEY_PAIR.getPublic()));

		Optional<PublicKey> account = accountService.getAccount(keyId);

		assertThat(account.isPresent(), is(true));
		assertThat(account.get(), is(KEY_PAIR.getPublic()));
	}

}
