package vaov.client;

import java.text.ParseException;

import vaov.client.vote.IllegalFormatException;
import vaov.client.vote.KeyException;
import vaov.client.vote.PrivateAccount;
import vaov.client.vote.VerificationException;
import vaov.client.writers.MessageWriterDebugImpl;

public abstract class Control {

	public static PrivateAccount getAccount(String username, char[] pass) throws KeyException {
		return AccountHandler.getAccount(username, pass);
	}

	public static void message(PrivateAccount acc, String target, String message) {
		try {
			MH.sendMessageToUser(target, message, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			throw new RuntimeException(e);
		}
	}

	public static void newAccount(String username, char[] pass) throws KeyException {
		AccountHandler.createNewAccount(username, pass);
	}

	public static boolean[] parseVoteString(String voteString) throws ParseException {
		boolean[] votes = new boolean[voteString.length()];
		String yesSymbols = "yYjJtT1";
		String noSymbols = "nNfF0";
		for (int i = 0; i < voteString.length(); i++ ) {
			String singleVoteString = String.valueOf(voteString.charAt(i));
			if (yesSymbols.contains(singleVoteString))
				votes[i] = true;
			else if (noSymbols.contains(singleVoteString))
				votes[i] = false;
			else
				throw new ParseException("voteString contains invalid characters.", i);
		}
		return votes;
	}

	public static void registerAccount(char[] pass, char[] privateKey, char[] publicKey) {
		// TODO
	}

	public static void vote(PrivateAccount acc, String targetID, String voteString) throws ParseException {
		vote(acc, new Vote(parseVoteString(voteString), targetID));
	}

	public static void vote(PrivateAccount acc, Vote vote) throws ParseException {
		try {
			MH.sendVote(vote, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			throw new RuntimeException(e);
		}
	}

	private static final MessageWriterDebugImpl MW = new MessageWriterDebugImpl();

	private static final MessageHandler MH = new MessageHandler(MW);

}
