package vaov.client.message;

import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;

import vaov.client.account.PrivateAccount;
import vaov.client.util.Config;
import vaov.client.util.HashComputer;
import vaov.remote.message.to.MessageContentTO;
import vaov.remote.message.to.MessageTO;
import vaov.remote.message.to.MessageToUserContentTO;
import vaov.remote.message.to.NewAccountContentTO;
import vaov.remote.message.to.NickChangeContentTO;
import vaov.remote.message.to.VoteContentTO;

public class MessageTOFactory {

	public static MessageTO createMessageTO(PrivateAccount privateauthor, MessageContentTO messageContent) {
		String digest = HashComputer.computeHash(messageContent);
		String signature = computeSignature(digest, privateauthor.getPrivateKey());

		MessageTO messageTO = new MessageTO();
		messageTO.setAuthor(privateauthor.getKeyId());
		messageTO.setDigest(digest);
		messageTO.setSignature(signature);
		messageTO.setContent(messageContent);
		return messageTO;
	}

	public static String marshalMessageContentTO(MessageContentTO message) {
		String result;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MessageContentTO.class, MessageToUserContentTO.class,
			NickChangeContentTO.class, VoteContentTO.class, NewAccountContentTO.class);
			StringWriter stringWriter = new StringWriter();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(message, stringWriter);
			result = stringWriter.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		return result;

	}

	private static String computeSignature(String digest, PrivateKey pk) {
		if (!(pk instanceof RSAPrivateKey)) {
			throw new RuntimeException("Key is not a RSAPrivateKey");
		}
		byte[] val;
		try {
			Cipher c = Cipher.getInstance(Config.getSignatureAlgorithm(), Config.getProvider());
			c.init(Cipher.ENCRYPT_MODE, pk);
			val = c.doFinal(digest.getBytes(Config.getCharset()));
		} catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException
		| InvalidKeyException e) {
			throw new RuntimeException(e);
		}
		return Base64.encodeBase64String(val);
	}

}
