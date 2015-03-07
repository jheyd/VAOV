package vaov.client.util;

import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;

import vaov.remote.message.to.MessageContentTO;

public class RsaHashComputer implements HashComputer {

	private static String computeHash(byte[]... input) {
		MessageDigest messageDigest = getMessageDigestInstance();

		for (byte[] singleInput : input) {
			messageDigest.update(singleInput);
		}

		return Base64.encodeBase64String(messageDigest.digest());
	}

	private static MessageDigest getMessageDigestInstance() {
		try {
			return MessageDigest.getInstance(Config.getHashAlgorithm(), Config.getProvider());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private static String marshal(Object object) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			StringWriter stringWriter = new StringWriter();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(object, stringWriter);
			return stringWriter.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Hashes the specified text and returns the computed digest
	 *
	 * @param messageContent
	 *            the messageContentTO to hash
	 * @return the computed hash (digest)
	 */
	@Override
	public String computeHash(MessageContentTO messageContent) {
		String marshalledMessageContent = marshal(messageContent);
		return computeHash(marshalledMessageContent.getBytes(Config.getCharset()));
	}

	/**
	 * Computes the Hash of the specified key.
	 *
	 * @param publicKey
	 *            the key for which to compute the hash.
	 * @return the computed hash
	 *         @
	 *         if the Key is not a RSA key
	 */
	@Override
	public String computeHash(PublicKey publicKey) {
		if (!(publicKey instanceof RSAPublicKey)) {
			throw new RuntimeException("Key is not an RSAPublicKey");
		}
		RSAPublicKey pub = (RSAPublicKey) publicKey;

		BigInteger modulus = pub.getModulus();
		BigInteger publicExponent = pub.getPublicExponent();
		return computeHash(modulus.toByteArray(), publicExponent.toByteArray());
	}

}
