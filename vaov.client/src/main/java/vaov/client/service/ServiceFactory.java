package vaov.client.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import vaov.remote.services.VaovMessageService;

public class ServiceFactory {
	public static VaovMessageService getMessageService() {
		URL url;
		try {
			url = new URL("http://localhost:8080/HelloWorld/hello?wsdl");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		QName qname = new QName("http://ws.mkyong.com/",
				"HelloWorldImplService");

		Service service = Service.create(url, qname);

		return service.getPort(VaovMessageService.class);

	}
}
