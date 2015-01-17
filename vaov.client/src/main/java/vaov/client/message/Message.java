package vaov.client.message;

import java.io.IOException;
import java.security.PublicKey;

import vaov.client.account.Account;
import vaov.client.account.PrivateAccount;
import vaov.client.account.PublishedAccounts;
import vaov.client.service.ServiceFactory;
import vaov.client.util.DigestComputer;
import vaov.client.util.Helper;
import vaov.client.util.IllegalFormatException;
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
public class Message {

	/** The message text */
	MessageContentTO message;
	/** The author of the message */
	Account author;

	/**
	 * Creates a new empty message. If the author is a {@link PrivateAccount},
	 * the message can be send.
	 *
	 * @param author
	 */
	public Message(Account author) {
		this.author = author;
	}

	/**
	 * Loads a message from a given input stream. Use this for received
	 * messages.
	 * 
	 * @param messageTO
	 *
	 * @throws IOException
	 *             if the stream could not be read properly.
	 * @throws KeyException
	 *             if something is fishy with the key.
	 * @throws IllegalFormatException
	 *             if the input data has the wrong format.
	 * @throws VerificationException
	 *             if the signature of the message could not be validated.
	 */
	public Message(MessageTO messageTO) throws IOException,
			IllegalFormatException, KeyException, VerificationException {

		PublicKey publicKey = new PublishedAccounts().getKey(messageTO
				.getAuthor());
		author = new Account(publicKey);
		message = messageTO.getContent();

		String computed_digest = DigestComputer.computeDigest(message);
		if (!computed_digest.equals(messageTO.getDigest()))
			throw new VerificationException(
					"Digest does not match to message. Message may be manipulated!");
		boolean verified = Helper.verifySignature(messageTO.getDigest(),
				messageTO.getSignature(), author.getPublicKey());
		if (!verified)
			throw new VerificationException(
					"Siganture does not match to message. Message may be manipulated!");
	}

	/**
	 * Returns the author of the message.
	 *
	 * @return the author
	 */
	public Account getAuthor() {
		return author;
	}

	/**
	 * Fetches the message text.
	 *
	 * @return the message text.
	 */
	public MessageContentTO getMessage() {
		return message;
	}

	/**
	 * Sends the message. To be precise, writes the content of the message to a
	 * PrintWriter which is then responsible for sending the message to its
	 * destination. Make sure that the PrintWriter is writing in UTF8!
	 * 
	 * @return
	 *
	 * @throws KeyException
	 *             if the author has not a private key, i.e. if the author is
	 *             not a {@link PrivateAccount}
	 */
	public boolean send() throws KeyException {
		if (!(author instanceof PrivateAccount))
			throw new KeyException("You cannot sign other peoples messages!");
		PrivateAccount privateauthor = (PrivateAccount) author;

		MessageTO messageTO = MessageTOFactory.createMessageTO(privateauthor,
				message);

		VaovMessageService messageService = ServiceFactory.getMessageService();

		return messageService.send(messageTO);
	}

	/**
	 * Sets the message text (without signature information etc.)
	 *
	 * @param message
	 *            the new message text.
	 */
	public void setMessage(MessageContentTO message) {
		this.message = message;
	}
}
