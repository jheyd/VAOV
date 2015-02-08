package vaov.client.message;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.PrivateKey;

import org.junit.Test;

import vaov.client.account.model.PrivateAccount;
import vaov.client.account.service.AccountService;
import vaov.client.util.HashComputer;
import vaov.remote.message.to.MessageTO;
import vaov.remote.message.to.MessageToUserContentTO;
import vaov.remote.services.KeyId;
import vaov.remote.services.VaovMessageService;
import de.janheyd.javalibs.test.LambdaArgumentMatcher;

public class MessageServiceTest {

	private AccountService accountService = mock(AccountService.class);
	private HashComputer hashComputer = mock(HashComputer.class);
	private VaovMessageService vaovMessageService = mock(VaovMessageService.class);
	private SignatureComputer signatureComputer = mock(SignatureComputer.class);
	private MessageService messageService = new MessageService(accountService, hashComputer, vaovMessageService,
		signatureComputer);

	@Test
	public void testSendMessageToUser() {
		String alias = "0";
		String digest = "digest";
		String signature = "signature";
		String message = "Hallo Welt!";
		PrivateAccount author = mock(PrivateAccount.class);
		PrivateKey privateKey = mock(PrivateKey.class);
		when(author.getPrivateKey()).thenReturn(privateKey);
		when(author.getKeyId()).thenReturn(mock(KeyId.class));
		when(signatureComputer.computeSignature(digest, privateKey)).thenReturn(signature);
		when(hashComputer.computeHash(any())).thenReturn(digest);

		messageService.sendMessageToUser(alias, message, author);

		MessageToUserContentTO content = new MessageToUserContentTO(alias, message);
		verify(vaovMessageService).send(argThat(new LambdaArgumentMatcher<MessageTO>(m -> {
			if (!m.getAuthor().equals(author.getKeyId())) {
				return false;
			}

			MessageToUserContentTO content2 = (MessageToUserContentTO) m.getContent();
			if (!content2.getUsername().equals(content.getUsername())) {
				return false;
			}
			if (!content2.getMessage().equals(content.getMessage())) {
				return false;
			}

			if (!m.getDigest().equals(digest)) {
				return false;
			}

			if (!m.getSignature().equals(signature)) {
				return false;
			}
			return true;
		})));

	}
}
