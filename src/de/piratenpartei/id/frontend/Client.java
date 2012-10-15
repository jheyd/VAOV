package de.piratenpartei.id.frontend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.piratenpartei.id.frontend.topic.Topic;
import de.piratenpartei.id.frontend.topic.TopicList;
import de.piratenpartei.id.frontend.topic.TopicManager;
import de.piratenpartei.id.vote.IllegalFormatException;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.Messenger;
import de.piratenpartei.id.vote.PrivateAccount;
import de.piratenpartei.id.vote.VerificationException;


/**
 * API for the Client
 * @author artus
 *
 */
public class Client {
		
	public static final String topicListFilePath = "topics.dat";

	public static void main(String[] args) {
		if(args.length > 1) execute(Arrays.copyOfRange(args, 1, args.length-1));
		else while(execute(Asker.askString("#: ").split(" ")));
	}

	/**
	 * Execute a command in String format
	 * @param args command to execute and parameters
	 * @return false if args[0] == "quit"
	 */
	public static boolean execute(String[] args) {
		/*
		 * commands:
		 * message <from> <to> <text>
		 * vote <from> <targetID> <vote>
		 * newIni <from> <topicID> <name> <text>
		 * listTopics
		 * showTopic <topicID>
		 * showIni <iniID>
		 * pull
		 * quit
		 */
		String[] commandNames = new String[]{
			"message", "vote", "newIni", "listTopics", "showTopic", "showIni", "pull", "quit", "newAccount", "registerAccount" };

		switch(Arrays.asList(commandNames).indexOf(args[0])){
		case(0):
			message(askAcc(args[1]),args[2],args[3]);
			break;
		case(1): 
			try {
				vote(askAcc(args[1]),args[2], args[3]);
			} catch (ParseException e) {
				System.out.println("Error while Parsing voteString at voteString[" + String.valueOf(e.getErrorOffset()) + "]: " + e.getMessage());
			}
			break;
		case(2):
			newIni(askAcc(args[1]),args[2], args[3], args[4]);
			break;
		case(3):
			List<String> l = listTopics();
			for(int i=0; i<l.size(); i++) System.out.println(l.get(i));
			break;
		case(4):
			List<String> list = getTopicInfo(args[1]);
			System.out.println("Tags: " + list.get(0));
			for(int i=1; i<list.size(); i++) System.out.println("Ini " + String.valueOf(i) + ": " + list.get(i));
			break;
		case(5):
			System.out.println(getIni(args[1]));
			break;
		case(6):	
			pull();
			break;
		case(7):  return false;
		case(8):
			String username = Asker.askString("Enter username for the Account: ");
			char[] pass = Asker.askCharArray("Enter password for the Account: "); 
			newAccount(username,pass);
			break;
		case(9):
			System.out.println("not yet implemented");
			//registerAccount();
			break;
		}
		return true;
	}

	/**
	 * Get PrivateAccount associated with a username form the KeyStore, asking for the password on the command line.
	 * @param username
	 * @return
	 */
	public static PrivateAccount askAcc(String username){
		// TODO
		// for testing only
		try { return new PrivateAccount(); }
		catch (KeyException e) { e.printStackTrace(); }
		return null;
		/*
		char[] pass = Asker.askCharArray("password for " + username + ": ");
		try {
			acc = new PrivateAccount(username,pass);
		} catch (KeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(pass != null)
			for(int i=0; i<pass.length; i++) pass[i] = 'a'; // overwrite password in memory
		*/
	}
	
	/**
	 * Register an already existing account
	 * @param username
	 * @param pass
	 * @return
	 */
	public static PrivateAccount registerAccount(String username, char[] pass, char[] privateKey, char[] publicKey) {
		// TODO
		try {
			PrivateAccount acc = new PrivateAccount();
			// acc = new PrivateAccount(privateKey,publicKey); TODO
			acc.store(username, pass);
			for(int i=0; i<pass.length; i++) pass[i] = 'a';
			for(int i=0; i<privateKey.length; i++) privateKey[i] = 'a';
			return acc;
		}
		catch (KeyException e) { e.printStackTrace(); }
		return null;
	}

	/**
	 * Generate a new Account and store it in the KeyStore
	 * @param username Username of the new Account
	 * @param pass Password of the new Account
	 * @return the new Account
	 */
	public static PrivateAccount newAccount(String username, char[] pass) {
		try {
			PrivateAccount acc = new PrivateAccount();
			acc.store(username, pass);
			for(int i=0; i<pass.length; i++) pass[i] = 'a';
			return acc;
		}
		catch (KeyException e) { e.printStackTrace(); }
		return null;
	}

	/**
	 * publicly sends a message to a user
	 * @param target the name of the user who shall receive the message
	 * @param message the message text
	 */
	public static void message(PrivateAccount acc, String target, String message) {
		Messenger m = new Messenger(acc);
		try { m.sendMessageToUser(target, message); }
		catch (IOException e)				{ e.printStackTrace(); }
		catch (IllegalFormatException e)	{ e.printStackTrace(); }
		catch (KeyException e)				{ e.printStackTrace(); }
		catch (VerificationException e)		{ e.printStackTrace(); }
	}
	
	/**
	 * vote a Topic
	 * @param targetID ID code of the targat Topic (typically somthing like "TOP12345")
	 * @param voteString the encoded vote String (typically something like "YYNYN")
	 */
	public static void vote(PrivateAccount acc, String targetID, String voteString) throws ParseException {
		Messenger m = new Messenger(acc);
		char[] buf = voteString.toCharArray();
		boolean[] votes = new boolean[buf.length];
		for(int i=0; i<buf.length; i++){
			switch(buf[i]){
			case('Y'): votes[i] = true;		break;
			case('N'): votes[i] = false;	break;
			default: throw new ParseException("voteString contains invalid characters.", i); 
			}
		}
		try { m.sendVote(new Vote(votes,targetID)); }
		catch (IOException e)				{ e.printStackTrace(); }
		catch (IllegalFormatException e)	{ e.printStackTrace(); }
		catch (KeyException e)				{ e.printStackTrace(); }
		catch (VerificationException e)		{ e.printStackTrace(); }
	}

	/**
	 * creates a new Ini
	 * @param topicID the ID of the Topic to create the Ini in or "new" to create the Ini in a new Topic
	 * @param name the name / title of the Ini to create
	 * @param text the text of the Ini
	 */
	public static void newIni(PrivateAccount acc, String topicID, String name, String text){
		Messenger m = new Messenger(acc);
		try { m.sendNewIni(name, text, topicID); }
		catch (IOException e)				{ e.printStackTrace(); }
		catch (IllegalFormatException e)	{ e.printStackTrace(); }
		catch (KeyException e)				{ e.printStackTrace(); }
		catch (VerificationException e)		{ e.printStackTrace(); }
	}
		
	public static List<String> listTopics() {
		TopicList tops = buildTopicList();
		List<String> l = new ArrayList<String>();
		List<Topic> t = tops.getTopics();
		for(int i=0; i<t.size(); i++) l.add("TOP" + String.valueOf(i) + ": " + t.get(i).getIniNames().get(0));
		return l;
	}
	
	/**
	 * 
	 * @param topicID ID of the Topic to show. Format: "TOP" + index
	 */
	public static List<String> getTopicInfo(String topicID) {
		TopicList tops = buildTopicList();
		int index = Integer.parseInt(topicID.substring(3));
		
		List<String> tags = tops.getTopics().get(index).getTags();
		String s="";
		for(int i=0; i<tags.size(); i++) s += tags.get(i) + ", ";
		
		List<String> result = new ArrayList<String>();
		result.add(s.substring(0,s.length()-2));
		
		result.addAll(tops.getTopics().get(index).getIniNames());
		return result;
	}
	
	/**
	 * 
	 * @param targetID ID of the Ini to show, Format: "INI" + topicIndex + "." + iniIndex
	 */
	public static String getIni(String targetID) {
		TopicList tops = buildTopicList();
		String[] indices = targetID.substring(3).split("\\.");
		int index1 = Integer.parseInt(indices[0]);
		int index2 = Integer.parseInt(indices[1]);
		return tops.getTopics().get(index1).getInis().get(index2).toString();
	}

	/**
	 * load TopicList data from server and write them to the file at topicListFilePath
	 */
	public static void pull() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Build a TopicList from the File at topicListFilePath;
	 * @return the built TopicList
	 */
	private static TopicList buildTopicList(){
		BufferedReader br;
		String s = "";
		String buf = "";
		try {
			br = new java.io.BufferedReader(new java.io.FileReader(TopicManager.topicListFilePath));
			while((buf = br.readLine()) != null) s += buf;
			br.close();
		}
		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); } 

		JSONObject jo = (JSONObject) JSONValue.parse(s);
		return new TopicList((JSONObject) jo.get("data"));
	}
}
