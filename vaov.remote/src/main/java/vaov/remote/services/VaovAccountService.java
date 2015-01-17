package vaov.remote.services;

import java.util.Collection;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import vaov.remote.account.to.AccountTO;

@WebService
@SOAPBinding(style = Style.RPC)
public interface VaovAccountService {

	@WebMethod
	Collection<AccountTO> getPublishedAccounts();

	@WebMethod
	AccountTO getAccount(String hash);
}
