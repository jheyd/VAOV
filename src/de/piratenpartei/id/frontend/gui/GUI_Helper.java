package de.piratenpartei.id.frontend.gui;


import java.text.ParseException;

import de.piratenpartei.id.frontend.Client;
import de.piratenpartei.id.frontend.Vote;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;


public class GUI_Helper {
	private PrivateAccount account;
	
	public GUI_Helper() {
		this.cryptInit();
	}
	
	public void cryptInit() {
		try {
			this.setAccount(this.queryAccount());
		} catch (KeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PrivateAccount queryAccount() throws KeyException {
		// TODO	
		PrivateAccount result;
		result = new PrivateAccount();
		return result;
	}

	public void vote(Vote vote) {
		try {
			Client.vote(getAccount(), vote);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getIniIDFromJListSelectedValue(String selectedValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrivateAccount getAccount() {
		return account;
	}

	public void setAccount(PrivateAccount account) {
		this.account = account;
	}

}
