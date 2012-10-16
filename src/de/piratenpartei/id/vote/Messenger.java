package de.piratenpartei.id.vote;

import java.io.*;

import org.json.simple.*;

import de.piratenpartei.id.frontend.Vote;


public class Messenger {
	public static PrintWriter outputStream = new PrintWriter(System.out);
	
	/**
	 * Send the a Message to the Server
	 * @param s the Message to send
	 * @param author the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	public static void sendMessage(String s, Account author) throws IOException, IllegalFormatException, KeyException, VerificationException{
		Message m = new Message(author);
		m.setMessage(s);
		m.send(outputStream);
		outputStream.flush();
	}

	/**
	 * Send a request to create a new Ini to the Server
	 * @param name the name to create the Ini with
	 * @param text the text to create the Ini with
	 * @param targetID the Topic to create the Ini in. If targetID == "new", create in new Topic
	 * @param author the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	@SuppressWarnings("unchecked")
	public static void sendNewIni(String name, String text, String targetID, Account author) throws IOException, IllegalFormatException, KeyException, VerificationException{
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("name", name);
		jo1.put("text", text);
		jo1.put("targetID", targetID);
		jo2.put("data", jo1);
		jo2.put("type", "newIni");
		sendMessage(jo2.toJSONString(),author);
	}
	
	/**
	 * Send a vote message to the Server
	 * @param vote the Vote to send
	 * @param author the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	@SuppressWarnings("unchecked")
	public static void sendVote(Vote vote, Account author) throws IOException, IllegalFormatException, KeyException, VerificationException{
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("vote", vote.toJSON());
		jo2.put("data", jo1);
		jo2.put("type", "vote");
		sendMessage(jo2.toJSONString(),author);
	}
	
	/**
	 * Publicly send a message to another user
	 * @param userName the name of the user to send the message to
	 * @param message the message to send
	 * @param author the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	@SuppressWarnings("unchecked")
	public static void sendMessageToUser(String userName, String message, Account author) throws IOException, IllegalFormatException, KeyException, VerificationException{
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("userName",userName);
		jo1.put("message",message);
		jo2.put("data",jo1);
		jo2.put("type","messageToUser");
		sendMessage(jo2.toJSONString(),author);
	}
	
	/**
	 * Request a nickname change from the server
	 * @param newNick the new nickname
	 * @param author the Account to sign the Message with
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws KeyException
	 * @throws VerificationException
	 */
	@SuppressWarnings("unchecked")
	public static void sendNickchange(String newNick, Account author) throws IOException, IllegalFormatException, KeyException, VerificationException{
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("newNick", newNick);
		jo2.put("data", jo1);
		jo2.put("type", "nickChange");
		sendMessage(jo2.toJSONString(),author);
	}
}
