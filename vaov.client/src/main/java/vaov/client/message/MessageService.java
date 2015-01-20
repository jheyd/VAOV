package vaov.client.message;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.binary.Base64;

import vaov.client.account.Account;
import vaov.client.account.PrivateAccount;
import vaov.client.account.PublishedAccountsService;
import vaov.client.service.ServiceFactory;
import vaov.client.util.Config;
import vaov.client.util.HashComputer;
import vaov.client.util.KeyException;
import vaov.client.util.MessageTOFactory;
import vaov.client.util.VerificationException;
import vaov.remote.message.to.MessageContentTO;
import vaov.remote.message.to.MessageTO;
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

	public static boolean verifyMessage(MessageTO message) {
		PublicKey publicKey = PublishedAccountsService.getKey(message.getAuthor());
		MessageContentTO content = message.getContent();

		String computed_digest = HashComputer.computeHash(content);
		if (!computed_digest.equals(message.getDigest())) {
			throw new VerificationException("Digest does not match to message. Message may be manipulated!");
		}
		return verifySignature(message.getDigest(), message.getSignature(), publicKey);
	}

	public static boolean send(MessageContentTO messageContent, PrivateAccount author) {
		MessageTO messageTO = MessageTOFactory.createMessageTO(author, messageContent);

		VaovMessageService messageService = ServiceFactory.getMessageService();

		return messageService.send(messageTO);
	}

	private static boolean verifySignature(String digest, String signature, PublicKey pk) {
		if (!(pk instanceof RSAPublicKey)) {
			throw new KeyException("Key is not a RSAPublicKey");
		}
		try {
			Cipher cipher = Config.getCipher();
			cipher.init(Cipher.DECRYPT_MODE, pk);
			byte[] val = cipher.doFinal(Base64.decodeBase64(signature));
			return digest.equals(new String(val, Config.CHARSET));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
