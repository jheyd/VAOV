package vaov.client.reduced.client;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

import vaov.client.reduced.client.writers.MessageWriter;
import vaov.client.reduced.client.writers.MessageWriterDefaultImpl;
import vaov.client.vote.IllegalFormatException;
import vaov.client.vote.KeyException;
import vaov.client.vote.PrivateAccount;
import vaov.client.vote.VerificationException;

public class Control {

	public static boolean[] parseVoteString(String voteString)
			throws ParseException {
		boolean[] votes = new boolean[voteString.length()];
		String yesSymbols = "yYjJtT1";
		String noSymbols = "nNfF0";
		for (int i = 0; i < voteString.length(); i++) {
			String singleVoteString = "" + voteString.charAt(i);
			if (yesSymbols.contains(singleVoteString))
				votes[i] = true;
			else if (noSymbols.contains(singleVoteString))
				votes[i] = false;
			else
				throw new ParseException(
						"voteString contains invalid characters.", i);
		}
		return votes;
	}

	AccountHandler a_handler;
	MessageHandler m_handler;

	public Control() {
		this(new MessageWriterDefaultImpl());
	}

	public Control(MessageWriter m) {
		a_handler = new AccountHandler();
		m_handler = new MessageHandler(m);
	}

	public PrivateAccount getAccount(String username, char[] pass)
			throws KeyException {
		a_handler.setKeyID(username);
		return a_handler.getAccount(pass);
	}

	public PrivateAccount getLoadedAccount(char[] pass) throws KeyException {
		return a_handler.getAccount(pass);
	}

	public void loadAccount(String string) {
		a_handler.setKeyID(string);

	}

	public void message(PrivateAccount acc, String target, String message) {
		try {
			System.out.println(acc.getPublicKey() instanceof RSAPublicKey);
			m_handler.sendMessageToUser(target, message, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void newAccount(String username, char[] pass) throws KeyException {
		a_handler.registerNewAccount(username, pass);
	}

	public void registerAccount(char[] pass, char[] privateKey, char[] publicKey) {
		// TODO
	}

	public void shutdown() {
		a_handler.shutdown();
	}

	public void vote(PrivateAccount acc, String targetID, String voteString)
			throws ParseException {
		vote(acc, new Vote(parseVoteString(voteString), targetID));
	}

	public void vote(PrivateAccount acc, Vote vote) throws ParseException {
		try {
			m_handler.sendVote(vote, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void voteWithLoadedAccount(Vote vote, char[] pass)
			throws KeyException, ParseException {
		PrivateAccount pa = a_handler.getAccount(pass);
		vote(pa, vote);
	}
}
