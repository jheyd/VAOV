package de.piratenpartei.id.frontend;


import java.io.IOException;
import java.security.KeyStore;

import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;


public class GUI_Helper {
	private VAOV v;
	private PrivateAccount account;
	private static final String keyId = "foo";
	public static final String readPath = "data.dat";

	public GUI_Helper() {
		this.cryptInit();
		this.vaovInit();
	}
	
	public void cryptInit() {
		try {
			this.account = this.queryAccount();
		} catch (KeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Helper.initKeyStore(password)
	}

	public void vaovInit(){
		try {
			v = new VAOV(this.account, readPath);
		} catch(IOException e){
			 throw new RuntimeException(e);
		}
		
	}
	
	public PrivateAccount queryAccount() throws KeyException {
		PrivateAccount result;
		result = new PrivateAccount();
		return null;
	}

}
