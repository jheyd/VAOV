package de.piratenpartei.id.frontend.model;

import de.piratenpartei.id.frontend.control.Util;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

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
