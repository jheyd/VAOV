package vaov.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.PublicKey;

import vaov.client.vote.Account;
import vaov.client.vote.IllegalFormatException;
import vaov.client.vote.KeyException;
import vaov.client.vote.Message;
import vaov.client.vote.MessageContentTO;
import vaov.client.vote.MessageToUserContentTO;
import vaov.client.vote.NewAccountContentTO;
import vaov.client.vote.NickChangeContentTO;
import vaov.client.vote.VerificationException;
import vaov.client.vote.VoteContentTO;
import vaov.client.writers.MessageWriter;

public class MessageHandler {

	// TODO replace JSON shit with JAXB

	public MessageWriter mw_output;

	public MessageHandler(MessageWriter m) {
		mw_output = m;
	}

	/**
	 * Send the a Message to the Server
	 *
	 * @param messageContent
	 *            the Message to send
	 * @param author
	 *            the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	private boolean sendMessage(MessageContentTO messageContent, Account author)
			throws IllegalFormatException, KeyException, VerificationException {
		PrintWriter pw_output = new PrintWriter(mw_output);
		Message m = new Message(author);
		m.setMessage(messageContent);
		m.send(pw_output);
		pw_output.flush();
		return mw_output.wasLastMessageSentSuccessfully();
	}

	/**
	 * Publicly send a message to another user
	 *
	 * @param userName
	 *            the name of the user to send the message to
	 * @param message
	 *            the message to send
	 * @param author
	 *            the Account to sign the Message with
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	public void sendMessageToUser(String userName, String message,
			Account author) throws IllegalFormatException, KeyException,
			VerificationException {
		MessageToUserContentTO content = new MessageToUserContentTO();
		content.setUsername(userName);
		content.setMessage(message);
		sendMessage(content, author);
	}

	/**
	 *
	 * @param acc
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	public void sendNewAccount(Account acc) throws IllegalFormatException,
			KeyException, VerificationException {
		PublicKey pk = acc.getPublicKey();
		String[] lines = pk.toString().split("\n");
		String modString = lines[1].split(" ")[3];
		String expString = lines[2].split(" ")[4];
		NewAccountContentTO content = new NewAccountContentTO(modString,
				expString);
		sendMessage(content, acc);
	}

	/**
	 * Request a nickname change from the server
	 *
	 * @param newNick
	 *            the new nickname
	 * @param author
	 *            the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	public void sendNickchange(String newNick, Account author)
			throws IllegalFormatException, KeyException, VerificationException {
		NickChangeContentTO content = new NickChangeContentTO(newNick);
		sendMessage(content, author);
	}

	/**
	 * Send a vote message to the Server
	 *
	 * @param vote
	 *            the Vote to send
	 * @param author
	 *            the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	public void sendVote(Vote vote, Account author)
			throws IllegalFormatException, KeyException, VerificationException {
		VoteContentTO content = new VoteContentTO(vote);
		sendMessage(content, author);
	}

}
