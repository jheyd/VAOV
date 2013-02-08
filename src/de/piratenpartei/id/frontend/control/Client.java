package de.piratenpartei.id.frontend.control;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

/**
 * API for the Client
 * @author dunkelzahn
 *
 */
public class Client {
		
	private Control control;
	public static final String topicListFilePath = "topics.dat";
	private PrintWriter outputWriter = new PrintWriter(System.out);

	public static void main(String[] args) {
		Client cli = new Client();
		cli.setOutput(new PrintWriter(System.out));
		if(args.length > 1){
			try {
				cli.execute(Arrays.copyOfRange(args, 1, args.length-1));
			} catch (WrongParameterCountException e) {
				System.out.println("Wrong parameter count!");
			}
		}
		else {
			try {
				while(cli.execute(Util.askString("#: ").split(" ")));
			} catch (WrongParameterCountException e) {
				System.out.println("Wrong parameter count!");
			}
		}
		cli.shutdown();
	}

	public void shutdown() {
		control.shutdown();
	}

	public Client() {
		control = new Control();
	}
	
	/**
	 * Execute a command in String format
	 * @param args command to execute and parameters
	 * @return false if args[0] == "quit"
	 * @throws WrongParameterCountException 
	 */
	public boolean execute(String[] args) throws WrongParameterCountException {
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
		case(0): // message
			if(args.length != 4) throw new WrongParameterCountException("wrong parameter count");
		control.message(askAcc(args[1]),args[2],args[3]);
			break;
		case(1): // vote
			if(args.length != 4) throw new WrongParameterCountException("wrong parameter count");
			try {
				control.vote(askAcc(args[1]),args[2], args[3]);
			} catch (ParseException e) {
				outputWriter.println("Error while Parsing voteString at voteString[" + String.valueOf(e.getErrorOffset()) + "]: " + e.getMessage());
			}
			break;
		case(2): // newIni
			if(args.length != 5) throw new WrongParameterCountException("wrong parameter count");
			try {
				control.newIni(askAcc(args[1]),args[2], args[3], args[4]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case(3): // listTopics
			if(args.length != 1) throw new WrongParameterCountException("wrong parameter count");
			List<String> l = control.listTopics();
			for(int i=0; i<l.size(); i++) outputWriter.println(l.get(i));
			break;
		case(4): // showTopic
			if(args.length != 2) throw new WrongParameterCountException("wrong parameter count");
			try{
				List<String> list = control.getTopicInfo(args[1]);
				outputWriter.println("Tags: " + list.get(0));
				for(int i=1; i<list.size(); i++) System.out.println("Ini " + String.valueOf(i) + ": " + list.get(i));
			} catch (IndexOutOfBoundsException e){
				outputWriter.println("Topic index too large!");
			} catch (ParseException e){
				outputWriter.println("\"" + args[1] + "\" is not a valid Topic ID");
			}
			break;
		case(5): // showIni
			if(args.length != 2) throw new WrongParameterCountException("wrong parameter count");
			try{
				outputWriter.println(control.getIni(args[1]));				
			} catch (IndexOutOfBoundsException e){
				outputWriter.println("Topic or Ini index too large!");
			} catch (ParseException e){
				outputWriter.println("\"" + args[1] + "\" is not a valid Ini ID");
			}
			break;
		case(6): // pull
			control.pull();
			break;
		case(7): // quit
			if(args.length != 1) throw new WrongParameterCountException("wrong parameter count");
			return false;
		case(8): // newAccount
			if(args.length != 1) throw new WrongParameterCountException("wrong parameter count");
			String username = Util.askString("Enter username for the Account: ");
			char[] pass = Util.askCharArray("Enter password for the Account: "); 
			try {
				control.registerNewAccount(username,pass);
			} catch (KeyException e) {
				outputWriter.println("KeyException in function \"newAccount\": " + e.getMessage());
			}
			break;
		case(9): // registerAccount
			outputWriter.println("not yet implemented");
			//registerAccount();
			break;
		}
		outputWriter.flush();
		return true;
	}

	/**
	 * Get PrivateAccount associated with a username form the KeyStore, asking for the password on the command line.
	 * @param username
	 * @return
	 */
	public PrivateAccount askAcc(String username){
		char[] pass = Util.askCharArray("password for " + username + ": ");
		PrivateAccount acc = control.getAcc(username, pass);
		if(pass != null)
			for(int i=0; i<pass.length; i++) pass[i] = 'a'; // overwrite password in memory
		return acc;
	}

	public PrintWriter getOutput() {
		return outputWriter;
	}

	public void setOutput(PrintWriter output) {
		this.outputWriter = output;
	}
	
	
}
