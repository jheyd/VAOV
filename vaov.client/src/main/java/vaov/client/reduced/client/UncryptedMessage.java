package vaov.client.reduced.client;

import java.io.PrintWriter;

import org.json.simple.JSONObject;

import vaov.client.vote.Helper;
import vaov.client.vote.KeyException;
import vaov.client.vote.PrivateAccount;

public class UncryptedMessage {

	/** The message text */
	String message;
	/** The author of the message */
	String author;

	/**
	 * Creates a new empty message. If the author is a {@link PrivateAccount},
	 * the message can be send.
	 * 
	 * @param author
	 */
	public UncryptedMessage(String author) {
		this.author = author;
		message = "";
	}

	@SuppressWarnings("unchecked")
	private String encodeData(String author, String digest, String signature,
			String message) {
		JSONObject jo = new JSONObject();
		jo.put("author", author);
		jo.put("digest", digest);
		jo.put("signature", signature);
		jo.put("message", message);
		return jo.toJSONString();
	}

	/**
	 * Returns the author of the message.
	 * 
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Fetches the message text.
	 * 
	 * @return the message text.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sends the message. To be precise, writes the content of the message to a
	 * PrintWriter which is then responsible for sending the message to its
	 * destination. Make sure that the PrintWriter is writing in UTF8!
	 * 
	 * @param ps
	 *            the PrintWriter to write the message to.
	 * @throws KeyException
	 *             if the author has not a private key, i.e. if the author is
	 *             not a {@link PrivateAccount}
	 */
	public void send(PrintWriter ps) throws KeyException {
		// make sure message ends with a new line
		if (!message.endsWith("\n"))
			message = message + "\n";
		String digest = Helper.computeDigest(message);
		String signature = "123456789";

		String enc = encodeData(author, digest, signature, message);
		ps.print(enc);
	}

	/**
	 * Sets the message text (without signature information etc.)
	 * 
	 * @param message
	 *            the new message text.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
