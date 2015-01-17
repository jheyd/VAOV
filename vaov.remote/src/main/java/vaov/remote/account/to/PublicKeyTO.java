package vaov.remote.account.to;

public class PublicKeyTO {

	private String modulus;
	private String exponent;

	public PublicKeyTO(String modulus, String exponent) {
		this.modulus = modulus;
		this.exponent = exponent;
	}

	public String getModulus() {
		return modulus;
	}

	public String getExponent() {
		return exponent;
	}

}
