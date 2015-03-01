package vaov.client;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import vaov.client.account.service.AccountService;
import vaov.client.message.MessageService;
import vaov.client.util.VoteParser;
import vaov.remote.services.KeyId;

public class ControlTest {

	private static final boolean[] VOTES = { false };
	private static final String VOTE_STRING = "vote";
	private static final String TARGET = "target";
	private static final String MESSAGE = "message";
	private static final Password PASSWORD = new Password("password".toCharArray());
	private static final String ALIAS = "alias";
	private static final KeyId KEY_ID = new KeyId(ALIAS);

	private PrivateAccount privateAccount = mock(PrivateAccount.class);
	private AccountService accountService = mock(AccountService.class);
	private MessageService messageService = mock(MessageService.class);
	private VoteParser voteParser = mock(VoteParser.class);
	private Control control = new Control(accountService, messageService, voteParser);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetAccount() throws Exception {
		Optional<PrivateAccount> accountOptional = Optional.of(privateAccount);
		when(accountService.getPrivateAccount(KEY_ID, PASSWORD)).thenReturn(accountOptional);

		Optional<PrivateAccount> account = control.getAccount(ALIAS, PASSWORD);

		assertThat(account, is(accountOptional));
	}

	@Test
	public void testMessage() throws Exception {
		control.message(privateAccount, ALIAS, MESSAGE);

		verify(messageService).sendMessageToUser(ALIAS, MESSAGE, privateAccount);
	}

	@Test
	public void testNewAccount() throws Exception {
		when(privateAccount.getAlias()).thenReturn(KEY_ID.getAlias());
		when(accountService.createNewAccount(PASSWORD)).thenReturn(privateAccount);

		String newAccount = control.newAccount(PASSWORD);

		assertThat(newAccount, is(KEY_ID.getAlias()));
	}

	@Test
	public void testVote() throws Exception {
		when(voteParser.parseVoteString(VOTE_STRING)).thenReturn(VOTES);

		control.vote(privateAccount, TARGET, VOTE_STRING);

		verify(messageService).sendVote(VOTES, TARGET, privateAccount);
	}

}
