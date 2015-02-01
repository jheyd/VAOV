package vaov.client.util;

import java.security.PublicKey;

import vaov.remote.account.to.PublicKeyTO;

public interface PublicKeyConverter {

	PublicKey readPublicKey(PublicKeyTO publicKeyTO);

	PublicKeyTO writePublicKey(PublicKey pk);

}