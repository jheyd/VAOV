package vaov.client.message;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import vaov.client.util.Config;

public class RsaSignatureComputer implements SignatureComputer {

	private static final String SIGNATURE_ALGORITHM = Config.getSignatureAlgorithm();
	private static final Provider PROVIDER = Config.getProvider();

	@Override
	public String computeSignature(String digest, PrivateKey privateKey) {
		if (!(privateKey instanceof RSAPrivateKey)) {
			throw new RuntimeException("privateKey is not an RSAPrivateKey");
		}
		byte[] val;
		try {
			Cipher c = Cipher.getInstance(SIGNATURE_ALGORITHM, PROVIDER);
			c.init(Cipher.ENCRYPT_MODE, privateKey);
			val = c.doFinal(digest.getBytes(Config.getCharset()));
		} catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException
			| InvalidKeyException e) {
			throw new RuntimeException(e);
		}
		return Base64.encodeBase64String(val);
	}

}
