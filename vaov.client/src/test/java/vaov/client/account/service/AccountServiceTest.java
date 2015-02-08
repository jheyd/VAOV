package vaov.client.account.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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

import vaov.client.account.model.Account;
import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import vaov.client.util.HashComputer;
import vaov.remote.services.KeyId;
import vaov.remote.services.VaovAccountService;
import de.janheyd.javalibs.test.LambdaArgumentMatcher;

public class AccountServiceTest {

	private static final String HASH = "1";
	private static final Password PASSWORD = new Password("password".toCharArray());
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
		when(accountCreationServiceMock.generateKeyPair()).thenReturn(KEY_PAIR);
		when(hashComputerMock.computeHash(any(PublicKey.class))).thenReturn(HASH);

		PrivateAccount createdAccount = accountService.createNewAccount(PASSWORD);

		verify(keystoreServiceMock).storeKeyPair(
		eq(createdAccount.getKeyId()),
		eq(PASSWORD),
		argThat(new LambdaArgumentMatcher<KeyPair>(kp -> {
			return kp.getPublic().equals(createdAccount.getPublicKey())
			&& kp.getPrivate().equals(createdAccount.getPrivateKey());
		})));

		assertThat(PASSWORD.isOverwritten(), is(true));
		assertThat(createdAccount.getKeyId().getAlias(), is(HASH));
	}

	@Test
	public void testGetAccount() {
		when(keystoreServiceMock.loadPublicKey(KEY_ID)).thenReturn(Optional.of(KEY_PAIR.getPublic()));

		Optional<Account> account = accountService.getAccount(KEY_ID);

		assertThat(account.isPresent(), is(true));
		assertThat(account.get().getPublicKey(), is(KEY_PAIR.getPublic()));
	}

	@Test
	public void testGetPrivateAccount() {
		when(keystoreServiceMock.loadKeyPair(KEY_ID, PASSWORD)).thenReturn(Optional.of(KEY_PAIR));

		Optional<PrivateAccount> account = accountService.getPrivateAccount(KEY_ID, PASSWORD);

		assertThat(account.isPresent(), is(true));
		assertThat(account.get().getKeyPair(), is(equalTo(KEY_PAIR)));
	}

}
