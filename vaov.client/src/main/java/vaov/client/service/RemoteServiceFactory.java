package vaov.client.service;

import vaov.remote.services.AccountRemoteService;
import vaov.remote.services.VaovMessageService;

public interface RemoteServiceFactory {

	public abstract AccountRemoteService getAccountRemoteService();

	public abstract VaovMessageService getMessageService();

}