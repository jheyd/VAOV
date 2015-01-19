package vaov.client;

import static java.lang.String.valueOf;

import java.text.ParseException;

import vaov.client.account.PrivateAccount;
import vaov.client.util.IllegalFormatException;
import vaov.client.util.KeyException;
import vaov.client.util.VerificationException;
import vaov.remote.services.KeyId;

public abstract class Control {

	private static final String NO_SYMBOLS = "nNfF0";
	private static final String YES_SYMBOLS = "yYjJtT1";

	public static PrivateAccount getAccount(String alias, char[] pass) throws KeyException {
		return AccountHandler.getAccount(new KeyId(alias), pass);
	}

	public static void message(PrivateAccount acc, String target, String message) {
		try {
			MessageHandler.sendMessageToUser(target, message, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			throw new RuntimeException(e);
		}
	}

	public static String newAccount(char[] pass) throws KeyException {
		return AccountHandler.createNewAccount(pass).getAlias();
	}

	public static boolean[] parseVoteString(String voteString) throws ParseException {
		boolean[] votes = new boolean[voteString.length()];
		for (int i = 0; i < voteString.length(); i++ ) {
			votes[i] = parseVote(voteString, i);
		}
		return votes;
	}

	private static boolean parseVote(String voteString, int index) throws ParseException {
		String singleVoteString = valueOf(voteString.charAt(index));
		if (YES_SYMBOLS.contains(singleVoteString)) {
			return true;
		}
		if (NO_SYMBOLS.contains(singleVoteString)) {
			return false;
		}

		throw new ParseException("voteString contains invalid characters.", index);
	}

	public static void vote(PrivateAccount acc, String targetID, String voteString) throws ParseException {
		vote(acc, parseVoteString(voteString), targetID);
	}

	public static void vote(PrivateAccount acc, boolean[] votes, String target) {
		try {
			MessageHandler.sendVote(votes, target, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			throw new RuntimeException(e);
		}
	}

}
