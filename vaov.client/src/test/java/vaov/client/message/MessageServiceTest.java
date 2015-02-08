package vaov.client.message;

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
import vaov.remote.services.VaovMessageService;

public class MessageServiceTest {

	private static final String MESSAGE = "Hallo Welt!";
	private static final String SIGNATURE = "signature";
	private static final String DIGEST = "digest";
	private static final String ALIAS = "0";
	private static final PrivateKey privateKey = mock(PrivateKey.class);
	private AccountService accountService = mock(AccountService.class);
	private HashComputer hashComputer = mock(HashComputer.class);
	private VaovMessageService vaovMessageService = mock(VaovMessageService.class);
	private SignatureComputer signatureComputer = mock(SignatureComputer.class);
	private MessageService messageService = new MessageService(accountService, hashComputer, vaovMessageService,
	signatureComputer);

	@Test
	public void testSendMessageToUser() {
		PrivateAccount author = new PrivateAccountBuilder().withPrivateKey(privateKey).create();
		when(signatureComputer.computeSignature(DIGEST, privateKey)).thenReturn(SIGNATURE);

		MessageToUserContentTO content = new MessageToUserContentTO(ALIAS, MESSAGE);
		MessageTO expectedMessageTo = new MessageTO(author.getKeyId(), DIGEST, SIGNATURE, content);
		when(hashComputer.computeHash(content)).thenReturn(DIGEST);

		messageService.sendMessageToUser(ALIAS, MESSAGE, author);

		verify(vaovMessageService).send(expectedMessageTo);
	}
}
