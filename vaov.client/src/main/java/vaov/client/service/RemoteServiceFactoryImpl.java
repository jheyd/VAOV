package vaov.client.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import vaov.remote.services.AccountRemoteService;
import vaov.remote.services.VaovMessageService;

public class RemoteServiceFactoryImpl implements RemoteServiceFactory {

	@Override
	public AccountRemoteService getAccountRemoteService() {
		URL url;
		try {
			url = new URL("http://localhost:8080/HelloWorld/hello?wsdl");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		// TODO jan 17.01.2015 fix URL
		QName qname = new QName("http://ws.mkyong.com/", "HelloWorldImplService");

		Service service = Service.create(url, qname);

		return service.getPort(AccountRemoteService.class);

	}

	@Override
	public VaovMessageService getMessageService() {
		URL url;
		try {
			url = new URL("http://localhost:8080/HelloWorld/hello?wsdl");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		// TODO jan 17.01.2015 fix URL
		QName qname = new QName("http://ws.mkyong.com/", "HelloWorldImplService");

		Service service = Service.create(url, qname);

		return service.getPort(VaovMessageService.class);

	}
}
