package vaov.client.service;

import java.util.Collection;

import vaov.remote.account.to.AccountTO;
import vaov.remote.services.AccountRemoteService;
import vaov.remote.services.KeyId;
import vaov.remote.services.VaovMessageService;

public class RemoteServiceDummyFactory implements RemoteServiceFactory {

	@Override
	public AccountRemoteService getAccountRemoteService() {
		return new AccountRemoteService() {

			@Override
			public AccountTO getAccount(KeyId keyId) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Collection<AccountTO> getPublishedAccounts() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	public VaovMessageService getMessageService() {
		return message -> {
			System.out.println(message);
			return true;
		};
	}

}
