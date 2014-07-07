package vaov.client.frontend.model;

import vaov.client.frontend.control.Util;
import vaov.client.vote.KeyException;
import vaov.client.vote.PrivateAccount;

public class AccountModel {
	String keyID;

	public PrivateAccount getPrivateAccount(char[] pass) throws KeyException {
		PrivateAccount pa = new PrivateAccount(keyID, pass);
		Util.overwriteChar(pass);
		return pa;
	}

	public void setPrivateAccount(String keyId) {
		this.keyID = keyId;
	}
}
