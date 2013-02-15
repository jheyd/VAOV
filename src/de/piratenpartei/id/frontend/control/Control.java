package de.piratenpartei.id.frontend.control;


import java.text.ParseException;
import java.util.List;

import de.piratenpartei.id.frontend.model.Vote;
import de.piratenpartei.id.vote.IllegalFormatException;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;
import de.piratenpartei.id.vote.VerificationException;


public class Control {
	AccountHandler a_handler;
	IniHandler i_handler;
	
	public Control() {
		this.a_handler = new AccountHandler();
		this.i_handler = new IniHandler();
	}
	
	public void voteWithLoadedAccount(Vote vote, char[] pass) throws KeyException, ParseException {
		PrivateAccount pa = a_handler.getAccount(pass); 
		vote(pa, vote);
	}

	public void message(PrivateAccount acc, String target, String message) {
		try {
			MessageHandler.sendMessageToUser(target, message, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void vote(PrivateAccount acc, Vote vote) throws ParseException {
		try {
			MessageHandler.sendVote(vote,acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void vote(PrivateAccount acc, String targetID, String voteString) throws ParseException {
		vote(acc, new Vote(parseVoteString(voteString),targetID));
	}

	public static boolean[] parseVoteString(String voteString) throws ParseException{
		boolean[] votes = new boolean[voteString.length()];
		for(int i=0; i<voteString.length(); i++){
			switch(voteString.charAt(i)){
			case('Y'): votes[i] = true;		break;
			case('y'): votes[i] = true;		break;
			case('N'): votes[i] = false;	break;
			case('n'): votes[i] = false;	break;
			default: throw new ParseException("voteString contains invalid characters.", i); 
			}
		}
		return votes;
	}
	
	public void registerNewAccount(String username, char[] pass) throws KeyException {
		a_handler.registerNewAccount(username, pass);
	}

	public PrivateAccount getAccount(String username, char[] pass) throws KeyException {
		a_handler.setAccount(username);
		return a_handler.getAccount(pass);
	}

	public PrivateAccount getLoadedAccount(char[] pass) throws KeyException {
		return a_handler.getAccount(pass);
	}

	public List<String> listTopics() {
		return i_handler.listTopics();
	}

	public List<String> getTopicInfo(String topicID) throws ParseException {
		return i_handler.getTopicInfo(topicID);
	}

	public void pull() {
		i_handler.pull();
	}

	public String getIni(String targetID) throws ParseException {
		return i_handler.getIni(targetID);
	}

	public void newIni(PrivateAccount acc, String topicID, String name,	String text) throws ParseException {
		i_handler.newIni(acc, topicID, name, text);
	}

	public void shutdown() {
		a_handler.shutdown();
		i_handler.shutdown();
	}
}
