package vaov.client;

import java.security.PublicKey;

import vaov.client.account.Account;
import vaov.client.account.PrivateAccount;
import vaov.client.message.MessageService;
import vaov.client.util.PublicKeyConverter;
import vaov.remote.account.to.PublicKeyTO;
import vaov.remote.message.to.MessageContentTO;
import vaov.remote.message.to.MessageToUserContentTO;
import vaov.remote.message.to.NewAccountContentTO;
import vaov.remote.message.to.NickChangeContentTO;
import vaov.remote.message.to.VoteContentTO;

public class MessageHandler {

	private boolean sendMessage(MessageContentTO messageContent, PrivateAccount author) {
		return MessageService.send(messageContent, author);
	}

	public void sendMessageToUser(String username, String message, PrivateAccount author) {
		MessageToUserContentTO content = new MessageToUserContentTO(username, message);
		sendMessage(content, author);
	}

	public void sendNewAccount(Account newAccount, PrivateAccount author) {
		PublicKey pk = newAccount.getPublicKey();
		PublicKeyTO publicKeyTO = PublicKeyConverter.writePublicKey(pk);

		NewAccountContentTO content = new NewAccountContentTO(publicKeyTO.getModulus(), publicKeyTO.getExponent());
		sendMessage(content, author);
	}

	public void sendNickchange(String newNick, PrivateAccount author) {
		NickChangeContentTO content = new NickChangeContentTO(newNick);
		sendMessage(content, author);
	}

	public void sendVote(boolean[] votes, String target, PrivateAccount author) {
		MessageContentTO vote = new VoteContentTO(votes, target);
		sendMessage(vote, author);
	}

}
