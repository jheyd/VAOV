package vaov.client;

import static java.lang.String.valueOf;

import java.text.ParseException;

import vaov.client.account.PrivateAccount;
import vaov.client.message.writers.MessageWriterDebugImpl;
import vaov.client.util.IllegalFormatException;
import vaov.client.util.KeyException;
import vaov.client.util.VerificationException;

public abstract class Control {

	private static final String NO_SYMBOLS = "nNfF0";
	private static final String YES_SYMBOLS = "yYjJtT1";

	public static PrivateAccount getAccount(String username, char[] pass)
			throws KeyException {
		return AccountHandler.getAccount(username, pass);
	}

	public static void message(PrivateAccount acc, String target, String message) {
		try {
			MH.sendMessageToUser(target, message, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			throw new RuntimeException(e);
		}
	}

	public static void newAccount(String username, char[] pass)
			throws KeyException {
		AccountHandler.createNewAccount(username, pass);
	}

	public static boolean[] parseVoteString(String voteString)
			throws ParseException {
		boolean[] votes = new boolean[voteString.length()];
		for (int i = 0; i < voteString.length(); i++) {
			votes[i] = parseVote(voteString, i);
		}
		return votes;
	}

	private static boolean parseVote(String voteString, int index)
			throws ParseException {
		String singleVoteString = valueOf(voteString.charAt(index));
		if (YES_SYMBOLS.contains(singleVoteString))
			return true;
		if (NO_SYMBOLS.contains(singleVoteString))
			return false;

		throw new ParseException("voteString contains invalid characters.",
				index);
	}

	public static void registerAccount(char[] pass, char[] privateKey,
			char[] publicKey) {
		// TODO
	}

	public static void vote(PrivateAccount acc, String targetID,
			String voteString) throws ParseException {
		vote(acc, parseVoteString(voteString), targetID);
	}

	public static void vote(PrivateAccount acc, boolean[] votes, String target)
			throws ParseException {
		try {
			MH.sendVote(votes, target, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			throw new RuntimeException(e);
		}
	}

	private static final MessageWriterDebugImpl MW = new MessageWriterDebugImpl();

	private static final MessageHandler MH = new MessageHandler(MW);

}
