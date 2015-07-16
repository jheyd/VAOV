package vaov.client.message;

import java.security.PublicKey;
import java.util.Optional;

import vaov.client.account.model.Account;
import vaov.client.account.model.PrivateAccount;
import vaov.client.account.model.PublicAccount;
import vaov.client.account.service.AccountService;
import vaov.client.service.RemoteServiceFactory;
import vaov.client.util.Config;
import vaov.client.util.HashComputer;
import vaov.client.util.RsaHashComputer;
import vaov.remote.account.to.PublicKeyTO;
import vaov.remote.message.to.MessageContentTO;
import vaov.remote.message.to.MessageTO;
import vaov.remote.message.to.MessageToUserContentTO;
import vaov.remote.message.to.NewAccountContentTO;
import vaov.remote.message.to.NickChangeContentTO;
import vaov.remote.message.to.VoteContentTO;
import vaov.remote.services.VaovMessageService;

public class MessageService {

	private AccountService accountService;
	private HashComputer hashComputer;
	private VaovMessageService messageService;
	private SignatureComputer signatureComputer;

	public MessageService(AccountService accountService, HashComputer hashComputer, VaovMessageService messageService,
		SignatureComputer signatureComputer) {
		this.accountService = accountService;
		this.hashComputer = hashComputer;
		this.messageService = messageService;
		this.signatureComputer = signatureComputer;

	}

	public MessageService(RemoteServiceFactory remoteServiceFactoryImpl) {
		this(AccountService.createAccountService(remoteServiceFactoryImpl), new RsaHashComputer(),
			remoteServiceFactoryImpl.getMessageService(), new RsaSignatureComputer());
	}

	public void sendMessageToUser(String alias, String message, PrivateAccount author) {
		MessageToUserContentTO content = new MessageToUserContentTO(alias, message);
		sendMessage(content, author);
	}

	public void sendNewAccount(PublicAccount newAccount, PrivateAccount author) { // NO_UCD (unused code)
		PublicKey pk = newAccount.getPublicKey();
		PublicKeyTO publicKeyTO = Config.getPublicKeyConverter().writePublicKey(pk);

		NewAccountContentTO content = new NewAccountContentTO(publicKeyTO.getModulus(), publicKeyTO.getExponent());
		sendMessage(content, author);
	}

	public void sendNickchange(String newNick, PrivateAccount author) { // NO_UCD (unused code)
		NickChangeContentTO content = new NickChangeContentTO(newNick);
		sendMessage(content, author);
	}

	public void sendVote(boolean[] votes, String target, PrivateAccount author) {
		MessageContentTO vote = new VoteContentTO(votes, target);
		sendMessage(vote, author);
	}

	public boolean verifyMessage(MessageTO message) { // NO_UCD (unused code)
		Optional<Account> account = accountService.getAccount(message.getAuthor());
		if (!account.isPresent())
			return false;
		PublicKey publicKey = account.get().getPublicKey();
		MessageContentTO content = message.getContent();

		String computed_digest = hashComputer.computeHash(content);
		if (!computed_digest.equals(message.getDigest()))
			// throw new
			// VerificationException("Digest does not match to message. Message may be manipulated!");
			return false;
		return signatureComputer.verifySignature(message.getDigest(), message.getSignature(), publicKey);
	}

	private MessageTO createMessageTO(PrivateAccount privateauthor, MessageContentTO messageContent) {
		String digest = hashComputer.computeHash(messageContent);
		String signature = signatureComputer.computeSignature(digest, privateauthor.getPrivateKey());

		MessageTO messageTO = new MessageTO();
		messageTO.setAuthor(privateauthor.getKeyId());
		messageTO.setDigest(digest);
		messageTO.setSignature(signature);
		messageTO.setContent(messageContent);
		return messageTO;
	}

	private boolean send(MessageContentTO messageContent, PrivateAccount author) {
		MessageTO messageTO = createMessageTO(author, messageContent);
		return messageService.send(messageTO);
	}

	private boolean sendMessage(MessageContentTO messageContent, PrivateAccount author) {
		return send(messageContent, author);
	}
}
