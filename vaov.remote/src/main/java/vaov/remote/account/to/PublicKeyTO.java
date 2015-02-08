package vaov.remote.account.to;

import vaov.remote.ValueObject;

public class PublicKeyTO extends ValueObject {

	private String modulus;
	private String exponent;

	public PublicKeyTO(String modulus, String exponent) {
		this.modulus = modulus;
		this.exponent = exponent;
	}

	public String getExponent() {
		return exponent;
	}

	public String getModulus() {
		return modulus;
	}

}
