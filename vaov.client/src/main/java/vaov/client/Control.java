package vaov.client;

import java.text.ParseException;
import java.util.Optional;

import vaov.client.account.AccountHandler;
import vaov.client.account.PrivateAccount;
import vaov.client.message.MessageHandler;
import vaov.client.util.Password;
import vaov.remote.services.KeyId;

public abstract class Control {

	public static Optional<PrivateAccount> getAccount(String alias, Password password)  {
		return AccountHandler.getAccount(new KeyId(alias), password);
	}

	public static void message(PrivateAccount acc, String target, String message) {
		MessageHandler.sendMessageToUser(target, message, acc);
	}

	public static String newAccount(Password pass)  {
		return AccountHandler.createNewAccount(pass).getAlias();
	}

	public static void vote(PrivateAccount acc, String targetID, String voteString) throws ParseException {
		boolean[] vote = VoteParser.parseVoteString(voteString);
		MessageHandler.sendVote(vote, targetID, acc);
	}
}