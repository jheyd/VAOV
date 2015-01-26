package vaov.client;

import java.text.ParseException;
import java.util.Optional;

import vaov.client.account.PrivateAccount;
import vaov.client.util.IllegalFormatException;
import vaov.client.util.KeyException;
import vaov.client.util.VerificationException;
import vaov.remote.services.KeyId;

public abstract class Control {

	public static Optional<PrivateAccount> getAccount(String alias, Password password) throws KeyException {
		return AccountHandler.getAccount(new KeyId(alias), password);
	}

	public static void message(PrivateAccount acc, String target, String message) {
		try {
			MessageHandler.sendMessageToUser(target, message, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			throw new RuntimeException(e);
		}
	}

	public static String newAccount(Password pass) throws KeyException {
		return AccountHandler.createNewAccount(pass).getAlias();
	}

	public static void vote(PrivateAccount acc, String targetID, String voteString) throws ParseException {
		boolean[] vote = VoteParser.parseVoteString(voteString);
		try {
			MessageHandler.sendVote(vote, targetID, acc);
		} catch (IllegalFormatException | KeyException | VerificationException e) {
			throw new RuntimeException(e);
		}
	}

}
