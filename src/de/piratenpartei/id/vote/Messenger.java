package de.piratenpartei.id.vote;

import java.io.*;

import org.json.simple.*;

import de.piratenpartei.id.frontend.Vote;


public class Messenger {
	private PrintWriter outputStream;
	private Account author;
	
	public Messenger(Account author){
		this.author = author;
		this.outputStream = new PrintWriter(System.out);
		this.outputStream.println("test");
	}
	
	public Messenger(Account author, String outputPath) throws FileNotFoundException{
		this.author = author;
		this.outputStream = new PrintWriter(outputPath);
	}
	
	public void sendMessage(String s) throws IOException, IllegalFormatException, KeyException, VerificationException{
		Message m = new Message(this.author);
		m.setMessage(s);
		m.send(this.outputStream);
		this.outputStream.flush();
	}

	@SuppressWarnings("unchecked")
	public void sendNewIni(String name, String text, String targetID) throws IOException, IllegalFormatException, KeyException, VerificationException{
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("name", name);
		jo1.put("text", text);
		jo1.put("targetID", targetID);
		jo2.put("data", jo1);
		jo2.put("type", "newIni");
		this.sendMessage(jo2.toJSONString());
	}
	@SuppressWarnings("unchecked")
	public void sendVote(Vote vote) throws IOException, IllegalFormatException, KeyException, VerificationException{
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("vote", vote.toJSON());
		jo2.put("data", jo1);
		jo2.put("type", "vote");
		this.sendMessage(jo2.toJSONString());
	}
	@SuppressWarnings("unchecked")
	public void sendMessageToUser(String userName, String message) throws IOException, IllegalFormatException, KeyException, VerificationException{
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("userName",userName);
		jo1.put("message",message);
		jo2.put("data",jo1);
		jo2.put("type","messageToUser");
		this.sendMessage(jo2.toJSONString());
	}
	@SuppressWarnings("unchecked")
	public void sendNickchange(String newNick) throws IOException, IllegalFormatException, KeyException, VerificationException{
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		jo1.put("newNick", newNick);
		jo2.put("data", jo1);
		jo2.put("type", "nickChange");
		this.sendMessage(jo2.toJSONString());
	}
}
