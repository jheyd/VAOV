package vaov.client.message;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface SignatureComputer {

	public String computeSignature(String digest, PrivateKey privateKey);

	boolean verifySignature(String digest, String signature, PublicKey pk);

}
