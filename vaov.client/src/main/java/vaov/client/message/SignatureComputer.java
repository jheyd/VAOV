package vaov.client.message;

import java.security.PrivateKey;

public interface SignatureComputer {

	public String computeSignature(String digest, PrivateKey privateKey);

}
