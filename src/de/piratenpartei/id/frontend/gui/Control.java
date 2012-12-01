package de.piratenpartei.id.frontend.gui;


import java.text.ParseException;

import de.piratenpartei.id.frontend.Model;
import de.piratenpartei.id.frontend.Vote;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;


public class Control {
	Model model;
	
	public Control() {
		this.model = new Model();
	}
	
	public PrivateAccount queryAccount() throws KeyException {
		// TODO	
		PrivateAccount result;
		result = new PrivateAccount();
		return result;
	}

	public void vote(Vote vote) {
		try {
			model.vote(getAccount(), vote);
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
		return model.getAccount();
	}

	public void loadAccount(String username, char[] pass) {
		try {
			this.model.setAccount(new PrivateAccount(username,pass));
		} catch (KeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void newAccount(String username, char[] pass) {
		try {
			this.model.setAccount(new PrivateAccount());
			this.model.getAccount().store(username, pass);
		} catch (KeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
