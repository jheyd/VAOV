package vaov.client;

import java.text.ParseException;
import java.util.Optional;

import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import vaov.client.account.service.AccountService;
import vaov.client.message.MessageService;
import vaov.client.util.VoteParser;
import vaov.remote.services.KeyId;

public abstract class Control {

	public static Optional<PrivateAccount> getAccount(String alias, Password password)  {
		return AccountService.getAccount(new KeyId(alias), password);
	}

	public static void message(PrivateAccount acc, String target, String message) {
		MessageService.sendMessageToUser(target, message, acc);
	}

	public static String newAccount(Password pass)  {
		return AccountService.createNewAccount(pass).getAlias();
	}

	public static void vote(PrivateAccount acc, String targetID, String voteString) throws ParseException {
		boolean[] vote = VoteParser.parseVoteString(voteString);
		MessageService.sendVote(vote, targetID, acc);
	}
}