package vaov.client.message;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.Test;

import vaov.client.account.model.PrivateAccount;
import vaov.client.account.service.AccountService;
import vaov.client.util.HashComputer;
import vaov.client.util.RsaHashComputer;
import vaov.remote.message.to.MessageContentTO;
import vaov.remote.message.to.MessageTO;
import vaov.remote.message.to.MessageToUserContentTO;
import vaov.remote.message.to.NickChangeContentTO;
import vaov.remote.message.to.VoteContentTO;
import vaov.remote.services.VaovMessageService;

public class MessageServiceTest {

	private static final String VOTE_TARGET = "target";
	private static final boolean[] VOTES = { true };
	private static final String NEW_NICK = "newNick";
	private static final String MESSAGE_TEXT = "Hallo Welt!";
	private static final String SIGNATURE = "signature";
	private static final String DIGEST = "digest";
	private static final String ALIAS = "0";
	private static final PrivateKey privateKey = mock(PrivateKey.class);
	private static final PublicKey publicKey = mock(PublicKey.class);
	private AccountService accountService = mock(AccountService.class);
	private HashComputer hashComputer = mock(RsaHashComputer.class);
	private VaovMessageService vaovMessageService = mock(VaovMessageService.class);
	private SignatureComputer signatureComputer = mock(SignatureComputer.class);
	private MessageService messageService = new MessageService(accountService, hashComputer, vaovMessageService,
		signatureComputer);

	@Test
	public void testSendMessageToUser() {
		MessageToUserContentTO content = new MessageToUserContentTO(ALIAS, MESSAGE_TEXT);
		testSendSomething(content, x -> messageService.sendMessageToUser(ALIAS, MESSAGE_TEXT, x));
	}

	@Test
	public void testSendNickChange() {
		NickChangeContentTO content = new NickChangeContentTO(NEW_NICK);
		testSendSomething(content, x -> messageService.sendNickchange(NEW_NICK, x));
	}

	@Test
	public void testSendVote() {
		VoteContentTO content = new VoteContentTO(VOTES, VOTE_TARGET);
		testSendSomething(content, x -> messageService.sendVote(VOTES, VOTE_TARGET, x));
	}

	@Test
	public void testVerifyMessageForMessageToUser() throws Exception {
		PrivateAccount author = new PrivateAccountBuilder().withPublicKey(publicKey).create();
		when(accountService.getAccount(author.getKeyId())).thenReturn(Optional.of(author));
		MessageContentTO content = new NickChangeContentTO(NEW_NICK);
		MessageTO message = new MessageTO(author.getKeyId(), DIGEST, SIGNATURE, content);
		when(hashComputer.computeHash(content)).thenReturn(DIGEST);

		messageService.verifyMessage(message);

		verify(signatureComputer).verifySignature(DIGEST, SIGNATURE, publicKey);
	}

	private void testSendSomething(MessageContentTO content, Consumer<PrivateAccount> consumer) {
		PrivateAccount author = new PrivateAccountBuilder().withPrivateKey(privateKey).create();
		when(signatureComputer.computeSignature(DIGEST, privateKey)).thenReturn(SIGNATURE);

		MessageTO expectedMessageTo = new MessageTO(author.getKeyId(), DIGEST, SIGNATURE, content);
		when(hashComputer.computeHash(content)).thenReturn(DIGEST);

		consumer.accept(author);

		verify(vaovMessageService).send(expectedMessageTo);

	}

}
