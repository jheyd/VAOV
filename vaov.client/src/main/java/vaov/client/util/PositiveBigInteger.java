package vaov.client.util;

import java.math.BigInteger;

import org.apache.commons.codec.binary.Base64;

public class PositiveBigInteger extends BigInteger {

	private static final long serialVersionUID = 1L;

	public PositiveBigInteger(BigInteger bigInteger) {
		super(bigInteger.toByteArray());
		if (bigInteger.compareTo(BigInteger.ZERO) <= 0) {
			throw new IllegalArgumentException("Input must be positive");
		}
	}

	public PositiveBigInteger(String base64String) {
		this(new BigInteger(Base64.decodeBase64(base64String)));
	}

	public String toBase64() {
		return Base64.encodeBase64String(toByteArray());
	}

}
