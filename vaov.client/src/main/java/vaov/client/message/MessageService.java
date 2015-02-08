package vaov.client.message;

import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;

import vaov.client.account.model.Account;
import vaov.client.account.model.PrivateAccount;
import vaov.client.account.service.AccountService;
import vaov.client.service.ServiceFactory;
import vaov.client.util.Config;
import vaov.client.util.HashComputer;
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

	public MessageService() {
		this(new AccountService(), new HashComputer(), ServiceFactory.getMessageService(), new RsaSignatureComputer());
	}

	public MessageService(AccountService accountService, HashComputer hashComputer, VaovMessageService messageService,
		SignatureComputer signatureComputer) {
		this.accountService = accountService;
		this.hashComputer = hashComputer;
		this.messageService = messageService;
		this.signatureComputer = signatureComputer;

	}

	public String marshalMessageContentTO(MessageContentTO message) {
		String result;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MessageContentTO.class, MessageToUserContentTO.class,
				NickChangeContentTO.class, VoteContentTO.class, NewAccountContentTO.class);
			StringWriter stringWriter = new StringWriter();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(message, stringWriter);
			result = stringWriter.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		return result;

	}

	public void sendMessageToUser(String alias, String message, PrivateAccount author) {
		MessageToUserContentTO content = new MessageToUserContentTO(alias, message);
		sendMessage(content, author);
	}

	public void sendNewAccount(Account newAccount, PrivateAccount author) {
		PublicKey pk = newAccount.getPublicKey();
		PublicKeyTO publicKeyTO = Config.getPublicKeyConverter().writePublicKey(pk);

		NewAccountContentTO content = new NewAccountContentTO(publicKeyTO.getModulus(), publicKeyTO.getExponent());
		sendMessage(content, author);
	}

	public void sendNickchange(String newNick, PrivateAccount author) {
		NickChangeContentTO content = new NickChangeContentTO(newNick);
		sendMessage(content, author);
	}

	public void sendVote(boolean[] votes, String target, PrivateAccount author) {
		MessageContentTO vote = new VoteContentTO(votes, target);
		sendMessage(vote, author);
	}

	public boolean verifyMessage(MessageTO message) {
		Optional<PublicKey> optional = accountService.getAccount(message.getAuthor());
		if (!optional.isPresent()) {
			return false;
		}
		PublicKey publicKey = optional.get();
		MessageContentTO content = message.getContent();

		String computed_digest = hashComputer.computeHash(content);
		if (!computed_digest.equals(message.getDigest())) {
			// throw new
			// VerificationException("Digest does not match to message. Message may be manipulated!");
			return false;
		}
		return verifySignature(message.getDigest(), message.getSignature(), publicKey);
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

	private boolean verifySignature(String digest, String signature, PublicKey pk) {
		if (!(pk instanceof RSAPublicKey)) {
			throw new RuntimeException("Key is not a RSAPublicKey");
		}
		try {
			Cipher cipher = Config.getCipher();
			cipher.init(Cipher.DECRYPT_MODE, pk);
			byte[] val = cipher.doFinal(Base64.decodeBase64(signature));
			return digest.equals(new String(val, Config.getCharset()));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}
}
