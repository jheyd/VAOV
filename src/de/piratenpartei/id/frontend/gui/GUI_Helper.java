package de.piratenpartei.id.frontend.gui;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import de.piratenpartei.id.frontend.VAOV;
import de.piratenpartei.id.frontend.Vote;
import de.piratenpartei.id.frontend.topic.Topic;
import de.piratenpartei.id.vote.IllegalFormatException;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;
import de.piratenpartei.id.vote.VerificationException;


public class GUI_Helper {
	public VAOV v;
	public Topic activeTopic;
	
	private PrivateAccount account;
	private static final String keyId = "foo";
	public static final String readPath = "data.dat";

	public GUI_Helper() {
		this.cryptInit();
		this.vaovInit();
		this.activeTopic = v.getTestTopic();
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
		v = new VAOV(true, this.account);
	}
	
	public PrivateAccount queryAccount() throws KeyException {
		PrivateAccount result;
		result = new PrivateAccount();
		return result;
	}

	public void vote(Vote vote) {
		try {
			v.vote(this.activeTopic, vote);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VerificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
