package vaov.client;

import java.io.IOException;
import java.security.PublicKey;

import vaov.client.account.Account;
import vaov.client.message.Message;
import vaov.client.util.IllegalFormatException;
import vaov.client.util.KeyException;
import vaov.client.util.VerificationException;
import vaov.remote.message.to.MessageContentTO;
import vaov.remote.message.to.MessageToUserContentTO;
import vaov.remote.message.to.NewAccountContentTO;
import vaov.remote.message.to.NickChangeContentTO;
import vaov.remote.message.to.VoteContentTO;

public class MessageHandler {

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
		Message m = new Message(author);
		m.setMessage(messageContent);
		return m.send();
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
	 * @param votes
	 * @param target
	 * @param author
	 *            the Account to sign the Message with
	 * @param vote
	 *            the Vote to send
	 *
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	public void sendVote(boolean[] votes, String target, Account author)
			throws IllegalFormatException, KeyException, VerificationException {
		MessageContentTO vote = new VoteContentTO(votes, target);
		sendMessage(vote, author);
	}

}
