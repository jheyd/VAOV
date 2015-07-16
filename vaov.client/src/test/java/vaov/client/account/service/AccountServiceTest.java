package vaov.client.account.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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

import vaov.client.account.model.Account;
import vaov.client.account.model.PrivateAccount;
import vaov.remote.services.AccountRemoteService;
import vaov.remote.services.KeyId;
import vaov.util.password.Password;
import vaov.util.test.LambdaArgumentMatcher;

public class AccountServiceTest {

	private static final String HASH = "hash";
	private static final KeyId KEY_ID = new KeyId(HASH);
	private static final KeyPair KEY_PAIR = new KeyPair(mock(PublicKey.class), mock(PrivateKey.class));
	private static final PrivateAccount PRIVATE_ACCOUNT = new PrivateAccount(KEY_ID, KEY_PAIR);
	private static final Password PASSWORD = new Password("password".toCharArray());

	private KeystoreService keystoreServiceMock = mock(KeystoreService.class);
	private AccountRemoteService vaovAccountServiceMock = mock(AccountRemoteService.class);
	private AccountCreationService accountCreationServiceMock = mock(AccountCreationService.class);
	private AccountService accountService = new AccountService(keystoreServiceMock, vaovAccountServiceMock,
		accountCreationServiceMock);

	@Test
	public void testCreateNewAccount() throws Exception {
		when(accountCreationServiceMock.createAccount()).thenReturn(PRIVATE_ACCOUNT);

		PrivateAccount createdAccount = accountService.createNewAccount(PASSWORD);

		verify(keystoreServiceMock).storeKeyPair(
			eq(createdAccount.getKeyId()),
			eq(PASSWORD),
			argThat(new LambdaArgumentMatcher<KeyPair>(kp -> {
				return kp.getPublic().equals(createdAccount.getPublicKey())
					&& kp.getPrivate().equals(createdAccount.getPrivateKey());
			})));

		assertThat(PASSWORD.isOverwritten(), is(true));
		assertThat(createdAccount, is(PRIVATE_ACCOUNT));
	}

	@Test
	public void testGetAccount() {
		when(keystoreServiceMock.loadPublicKey(KEY_ID)).thenReturn(Optional.of(KEY_PAIR.getPublic()));

		Optional<Account> account = accountService.getAccount(KEY_ID);

		assertThat(account.isPresent(), is(true));
		assertThat(account.get().getPublicKey(), is(KEY_PAIR.getPublic()));
	}

	@Test
	public void testGetPrivateAccount() throws Exception {
		when(keystoreServiceMock.loadKeyPair(KEY_ID, PASSWORD)).thenReturn(Optional.of(KEY_PAIR));

		Optional<PrivateAccount> account = accountService.getPrivateAccount(KEY_ID, PASSWORD);

		assertThat(account.isPresent(), is(true));
		assertThat(PASSWORD.isOverwritten(), is(true));
		assertThat(account.get().getKeyPair(), is(equalTo(KEY_PAIR)));
	}

}
