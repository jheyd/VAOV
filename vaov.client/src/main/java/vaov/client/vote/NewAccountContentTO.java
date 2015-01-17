package vaov.client.vote;

public class NewAccountContentTO extends MessageContentTO {

	private String modulus;
	private String publicExponent;

	public String getModulus() {
		return modulus;
	}

	public String getPublicExponent() {
		return publicExponent;
	}

	public NewAccountContentTO(String modString, String expString) {
		this.modulus = modString;
		this.publicExponent = expString;
	}

}
