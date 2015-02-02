package vaov.client.message;

import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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

/**
 * A signed message that is used to communicate with any kind of service. Every
 * Message is associated to an {@link Account}. The account supplies the key for
 * verifying the message.
 *
 * Messages that were created by a {@link PrivateAccount} can be send. The
 * sending process adds a signature to the message text.
 *
 * @author arne
 *
 */
public class MessageService {

	public static String marshalMessageContentTO(MessageContentTO message) {
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

	public static void sendMessageToUser(String alias, String message, PrivateAccount author) {
		MessageToUserContentTO content = new MessageToUserContentTO(alias, message);
		sendMessage(content, author);
	}

	public static void sendNewAccount(Account newAccount, PrivateAccount author) {
		PublicKey pk = newAccount.getPublicKey();
		PublicKeyTO publicKeyTO = Config.getPublicKeyConverter().writePublicKey(pk);

		NewAccountContentTO content = new NewAccountContentTO(publicKeyTO.getModulus(), publicKeyTO.getExponent());
		sendMessage(content, author);
	}

	public static void sendNickchange(String newNick, PrivateAccount author) {
		NickChangeContentTO content = new NickChangeContentTO(newNick);
		sendMessage(content, author);
	}

	public static void sendVote(boolean[] votes, String target, PrivateAccount author) {
		MessageContentTO vote = new VoteContentTO(votes, target);
		sendMessage(vote, author);
	}

	public static boolean verifyMessage(MessageTO message) {
		Optional<PublicKey> optional = AccountService.getKey(message.getAuthor());
		if (!optional.isPresent()) {
			return false;
		}
		PublicKey publicKey = optional.get();
		MessageContentTO content = message.getContent();

		String computed_digest = HashComputer.computeHash(content);
		if (!computed_digest.equals(message.getDigest())) {
			// throw new
			// VerificationException("Digest does not match to message. Message may be manipulated!");
			return false;
		}
		return verifySignature(message.getDigest(), message.getSignature(), publicKey);
	}

	private static String computeSignature(String digest, PrivateKey pk) {
		if (!(pk instanceof RSAPrivateKey)) {
			throw new RuntimeException("Key is not a RSAPrivateKey");
		}
		byte[] val;
		try {
			Cipher c = Cipher.getInstance(Config.getSignatureAlgorithm(), Config.getProvider());
			c.init(Cipher.ENCRYPT_MODE, pk);
			val = c.doFinal(digest.getBytes(Config.getCharset()));
		} catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException
		| InvalidKeyException e) {
			throw new RuntimeException(e);
		}
		return Base64.encodeBase64String(val);
	}

	private static MessageTO createMessageTO(PrivateAccount privateauthor, MessageContentTO messageContent) {
		String digest = HashComputer.computeHash(messageContent);
		String signature = computeSignature(digest, privateauthor.getPrivateKey());

		MessageTO messageTO = new MessageTO();
		messageTO.setAuthor(privateauthor.getKeyId());
		messageTO.setDigest(digest);
		messageTO.setSignature(signature);
		messageTO.setContent(messageContent);
		return messageTO;
	}

	private static boolean send(MessageContentTO messageContent, PrivateAccount author) {
		MessageTO messageTO = MessageService.createMessageTO(author, messageContent);

		VaovMessageService messageService = ServiceFactory.getMessageService();

		return messageService.send(messageTO);
	}

	private static boolean sendMessage(MessageContentTO messageContent, PrivateAccount author) {
		return send(messageContent, author);
	}

	private static boolean verifySignature(String digest, String signature, PublicKey pk) {
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
