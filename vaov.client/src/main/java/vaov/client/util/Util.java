package vaov.client.util;

import java.io.StringWriter;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import vaov.client.Control;
import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import de.janheyd.javalibs.ask.AskUtils;

public class Util {

	/**
	 * Get PrivateAccount associated with a username form the KeyStore, asking
	 * for the password on the command line.
	 *
	 * @param alias
	 * @return
	 *         @
	 */
	public static Optional<PrivateAccount> askAcc(String alias) {
		Password password = Util.askPassword("password for " + alias + ": ");
		Optional<PrivateAccount> acc = new Control().getAccount(alias, password);
		password.overwrite();
		return acc;
	}

	public static Password askPassword(String question) {
		char[] buf = new char[256];
		AskUtils.askCharArray(question, buf);
		return new Password(buf);
	}

	/**
	 * overwrites a char Array with zeroes
	 *
	 * @param c
	 *            the Array to overwrite
	 */
	public static void overwriteCharArray(char[] c) {
		for (int i = 0; i < c.length; i++ ) {
			c[i] = 0;
		}
	}

	public static String marshal(Object object) {
		String result;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			StringWriter stringWriter = new StringWriter();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(object, stringWriter);
			result = stringWriter.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		return result;

	}
}
