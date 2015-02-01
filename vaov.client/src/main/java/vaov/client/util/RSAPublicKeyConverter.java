package vaov.client.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import vaov.remote.account.to.PublicKeyTO;

public class RSAPublicKeyConverter implements PublicKeyConverter {

	@Override
	public PublicKey readPublicKey(PublicKeyTO publicKeyTO) {
		PositiveBigInteger modulus = new PositiveBigInteger(publicKeyTO.getModulus());
		PositiveBigInteger exponent = new PositiveBigInteger(publicKeyTO.getExponent());

		RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
		PublicKey publicKey;
		try {
			// TODO: specify security provider
			KeyFactory fact = KeyFactory.getInstance("RSA", Config.getProvider());
			publicKey = fact.generatePublic(spec);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("No security provider for RSA algorithm", e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException("Unable to generate public key", e);
		}
		return publicKey;
	}

	@Override
	public PublicKeyTO writePublicKey(PublicKey pk) {
		if (pk instanceof RSAPublicKey) {
			RSAPublicKey rsaPublicKey = (RSAPublicKey) pk;
			String modulus = new PositiveBigInteger(rsaPublicKey.getModulus()).toBase64();
			String exponent = new PositiveBigInteger(rsaPublicKey.getPublicExponent()).toBase64();
			return new PublicKeyTO(modulus, exponent);
		}
		throw new IllegalArgumentException("not an RSAPublicKey");
	}

}
