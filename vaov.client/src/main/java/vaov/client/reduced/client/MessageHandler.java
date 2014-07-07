package vaov.client.reduced.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import org.json.simple.JSONObject;

import vaov.client.reduced.client.writers.MessageWriter;
import vaov.client.vote.Account;
import vaov.client.vote.IllegalFormatException;
import vaov.client.vote.KeyException;
import vaov.client.vote.Message;
import vaov.client.vote.VerificationException;

public class MessageHandler {

	public MessageWriter mw_output;

	public PrintWriter pw_output;

	public MessageHandler(MessageWriter m) {
		mw_output = m;
		pw_output = new PrintWriter(mw_output);
	}

	/**
	 * Send the a Message to the Server
	 * 
	 * @param s
	 *            the Message to send
	 * @param author
	 *            the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	private boolean sendMessage(String s, Account author)
			throws IllegalFormatException, KeyException, VerificationException {
		System.out.println(author.getPublicKey() instanceof RSAPublicKey);
		Message m = new Message(author);
		m.setMessage(s);
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
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	@SuppressWarnings("unchecked")
	public void sendMessageToUser(String userName, String message,
			Account author) throws IllegalFormatException, KeyException,
			VerificationException {
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("userName", userName);
		jo1.put("message", message);
		jo2.put("data", jo1);
		jo2.put("type", "messageToUser");
		System.out.println(author.getPublicKey() instanceof RSAPublicKey);
		sendMessage(jo2.toJSONString(), author);
	}

	// /**
	// * Send a request to create a new Ini to the Server
	// * @param name the name to create the Ini with
	// * @param text the text to create the Ini with
	// * @param targetID the Topic to create the Ini in.
	// * @param author the Account to sign the Message with
	// * @throws IOException
	// * @throws IllegalFormatException
	// * @throws KeyException
	// * @throws VerificationException
	// */
	// @SuppressWarnings("unchecked")
	// public static void sendNewIni(String name, String text, String targetID,
	// Account author) throws IllegalFormatException, KeyException,
	// VerificationException{
	// JSONObject jo1 = new JSONObject();
	// JSONObject jo2 = new JSONObject();
	// jo1.put("name", name);
	// jo1.put("text", text);
	// jo1.put("targetID", targetID);
	// jo2.put("data", jo1);
	// jo2.put("type", "newIni");
	// sendMessage(jo2.toJSONString(),author);
	// }
	//
	// /**
	// * Send a request to create a new Ini in a new Topic to the Server
	// * @param name the name to create the Ini with
	// * @param text the text to create the Ini with
	// * @param tags the tags to create the Topic with
	// * @param author the Account to sign the Message with
	// * @throws IOException
	// * @throws IllegalFormatException
	// * @throws KeyException
	// * @throws VerificationException
	// */
	// @SuppressWarnings("unchecked")
	// public static void sendNewTopic(String name, String text, String tags,
	// Account author) throws IllegalFormatException, KeyException,
	// VerificationException{
	// JSONObject jo1 = new JSONObject();
	// JSONObject jo2 = new JSONObject();
	// jo1.put("name", name);
	// jo1.put("text", text);
	// jo1.put("tags", tags);
	// jo2.put("data", jo1);
	// jo2.put("type", "newTopic");
	// sendMessage(jo2.toJSONString(),author);
	// }

	/**
	 * 
	 * @param acc
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	@SuppressWarnings("unchecked")
	public void sendNewAccount(Account acc) throws IllegalFormatException,
			KeyException, VerificationException {
		PublicKey pk = acc.getPublicKey();
		String[] lines = pk.toString().split("\n");
		String modString = lines[1].split(" ")[3];
		String expString = lines[2].split(" ")[4];
		JSONObject jo = new JSONObject();
		jo.put("modulus", modString);
		jo.put("public exponent", expString);
		sendMessage(jo.toJSONString(), acc);
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
	@SuppressWarnings("unchecked")
	public void sendNickchange(String newNick, Account author)
			throws IllegalFormatException, KeyException, VerificationException {
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("newNick", newNick);
		jo2.put("data", jo1);
		jo2.put("type", "nickChange");
		sendMessage(jo2.toJSONString(), author);
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
	@SuppressWarnings("unchecked")
	public void sendVote(Vote vote, Account author)
			throws IllegalFormatException, KeyException, VerificationException {
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("vote", vote.toJSONString());
		jo2.put("data", jo1);
		jo2.put("type", "vote");
		sendMessage(jo2.toJSONString(), author);
	}

}
