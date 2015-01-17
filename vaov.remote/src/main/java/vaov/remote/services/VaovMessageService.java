package vaov.remote.services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import vaov.remote.message.to.MessageTO;

@WebService
@SOAPBinding(style = Style.RPC)
public interface VaovMessageService {

	@WebMethod
	boolean send(MessageTO message);
}
