package vaov.client;

import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.util.Optional;

import vaov.client.account.model.PrivateAccount;
import vaov.client.account.service.AccountService;
import vaov.client.message.MessageService;
import vaov.client.service.RemoteServiceFactory;
import vaov.client.util.VoteParser;
import vaov.remote.services.KeyId;
import vaov.util.password.Password;

public class Control {

	private AccountService accountService;
	private MessageService messageService;
	private VoteParser voteParser;

	public Control(AccountService accountService, MessageService messageService, VoteParser voteParser) {
		super();
		this.accountService = accountService;
		this.messageService = messageService;
		this.voteParser = voteParser;
	}

	public Control(RemoteServiceFactory remoteServiceFactoryImpl) {

		this(AccountService.createAccountService(remoteServiceFactoryImpl),
			new MessageService(remoteServiceFactoryImpl), new VoteParser());
	}

	public Optional<PrivateAccount> getAccount(String alias, Password password) throws UnrecoverableKeyException {
		return accountService.getPrivateAccount(new KeyId(alias), password);
	}

	public void message(PrivateAccount acc, String target, String message) {
		messageService.sendMessageToUser(target, message, acc);
	}

	public String newAccount(Password pass) throws UnrecoverableKeyException {
		return accountService.createNewAccount(pass).getAlias();
	}

	public void vote(PrivateAccount acc, String targetID, String voteString) throws ParseException {
		boolean[] vote = voteParser.parseVoteString(voteString);
		messageService.sendVote(vote, targetID, acc);
	}
}