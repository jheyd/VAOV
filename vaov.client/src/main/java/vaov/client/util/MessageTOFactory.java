package vaov.client.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
import vaov.remote.message.to.MessageContentTO;
import vaov.remote.message.to.MessageTO;
import vaov.remote.message.to.MessageToUserContentTO;
import vaov.remote.message.to.NewAccountContentTO;
import vaov.remote.message.to.NickChangeContentTO;
import vaov.remote.message.to.VoteContentTO;

public class MessageTOFactory {

	static String marshalMessageContentTO(MessageContentTO message) {
		String result;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(
					MessageContentTO.class, MessageToUserContentTO.class,
					NickChangeContentTO.class, VoteContentTO.class,
					NewAccountContentTO.class);
			StringWriter stringWriter = new StringWriter();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.marshal(message, stringWriter);
			result = stringWriter.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		return result;

	}

	/**
	 * Encrypts a given string with the specified private key.
	 *
	 * @param digest
	 *            the text to encrypt
	 * @param pk
	 *            the private key to use
	 * @return the encoded string.
	 * @throws KeyException
	 */
	private static String computeSignature(String digest, PrivateKey pk)
			throws KeyException {
		if (!(pk instanceof RSAPrivateKey))
			throw new KeyException("Key is not a RSAPrivateKey");
		byte[] val;
		try {
			Cipher c = Cipher.getInstance(Config.SIGNATURE_ALGORITHM,
					Config.getProvider());
			c.init(Cipher.ENCRYPT_MODE, pk);
			val = c.doFinal(digest.getBytes(Config.CHARSET));
		} catch (InvalidKeyException e) {
			throw new KeyException("Key is not a valid Key", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		String encoded = Base64.encodeBase64String(val);
		return encoded;
	}

	public static MessageTO createMessageTO(PrivateAccount privateauthor,
			MessageContentTO messageContent) throws KeyException {
		String digest = DigestComputer.computeDigest(messageContent);
		String signature = computeSignature(digest,
				privateauthor.getPrivateKey());

		MessageTO messageTO = new MessageTO();
		messageTO.setAuthor(HashComputer.computeHash(privateauthor
				.getPublicKey()));
		messageTO.setDigest(digest);
		messageTO.setSignature(signature);
		messageTO.setContent(messageContent);
		return messageTO;
	}

}
