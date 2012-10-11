package de.piratenpartei.id.vote;

import java.io.IOException;
import java.util.Arrays;

import de.piratenpartei.id.frontend.Asker;
import de.piratenpartei.id.frontend.Vote;

/**
 * This class manages the voting part of this application: Key and Account Management, sending signed Messages, controlling Signatures of other Messages.
 * Dies ist ein Versuch, Abstimmungs- und Antragsanzeigetool voneinander zu trennen.
 * Diese Klasse ist  darauf ausgelegt, allein ausfühbar und mittels Texteingaben steuerbar zu sein (bspw. ID eines Antrags eingeben um über diese Antrag abzustimmen).
 * 
 * @author artus
 *
 */

public class VoteManager {
	public static final String[] commandNames = new String[]{ "message" , "vote" , "newIni" , "newTopic"};
	public static final String[] commandShortNames = new String[]{ "m" , "v" , "i" , "t" };
	
	private PrivateAccount acc;
		
	public static void main(String[] args) {
		VoteManager vm = new VoteManager();
		if(args.length > 1){ // batch mode
			vm.execute(Arrays.copyOfRange(args, 1, args.length-1));
		} else { // interactive mode
			//TODO
		}
	}

	private void execute(String[] args) {
		switch(Arrays.asList(commandNames).indexOf(args[0])){
		case(0): // message
			this.setAcc(args[1]);
			try {
				this.message(args[2],args[3]);
			} catch (KeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (VerificationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case(1): // vote
			this.setAcc(args[1]);
			try {
				this.vote(args[2], args[3]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (VerificationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case(2): // newIni
			this.setAcc(args[1]);
			this.newIni(args[2], args[3], args[4]);
			break;
			
		case(3): // newTopic
			this.setAcc(args[1]);
			this.newTopic(args[2], args[3]);
			break;
			
		default:
		}
	}
	
	private void setAcc(String username){
		char[] pass = Asker.askCharArray("password for " + username + ": ");
		try {
			acc = new PrivateAccount(username,pass);
		} catch (KeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(pass != null)
			for(int i=0; i<pass.length; i++) pass[i] = 'a'; // overwrite password in memory
	}
	
	/**
	 * publicly sends a message to a user
	 * @param target the name of the user who shall receive the message
	 * @param message the message text
	 * @throws KeyException 
	 * @throws VerificationException 
	 * @throws IllegalFormatException 
	 * @throws IOException 
	 */
	public void message(String target, String message) throws KeyException, IOException, IllegalFormatException, VerificationException{
		Messenger m = new Messenger(this.acc);
		m.sendMessageToUser(target, message);
	}
	
	/**
	 * vote a Topic
	 * @param targetID ID code of the targat Topic (typically somthing like "TOP12345")
	 * @param voteString the encoded vote String (typically something like "YYNYN")
	 * @throws VerificationException 
	 * @throws KeyException 
	 * @throws IllegalFormatException 
	 * @throws IOException 
	 */
	public void vote(String targetID, String voteString) throws IOException, IllegalFormatException, KeyException, VerificationException{
		Messenger m = new Messenger(this.acc);
		char[] buf = voteString.toCharArray();
		boolean[] votes = new boolean[buf.length];
		for(int i=0; i<buf.length; i++){
			switch(buf[i]){
			case('Y'):
				votes[i] = true;
				break;
			case('N'):
				votes[i] = false;
				break;
			default:
				System.out.println("voteString contains invalid characters.");
				return;
			}
		}
		m.sendVote(new Vote(votes,targetID));
	}

	/**
	 * creates a new Ini in an existing Topic
	 * @param topicID the ID of the Topic to create the Ini in
	 * @param name the name / title of the Ini to create
	 * @param text the text of the Ini
	 */
	public void newIni(String topicID, String name, String text){
		// TODO
	}
	/**
	 * creates a new Ini in a new Topic
	 * @param name the name / title of the Ini to create
	 * @param text the text of the Ini
	 */
	public void newTopic(String name, String text){
		// TODO
	}
}
