package de.piratenpartei.id.test;

import java.security.PublicKey;

import org.json.simple.JSONObject;

import de.piratenpartei.id.vote.Account;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

public class TestFoo {
	public static void main(String[] args) {
		try {
			PublicKey pk = new PrivateAccount().getPublicKey();
			String[] lines = pk.toString().split("\n");
			String modString = lines[1].split(" ")[3];
			String expString = lines[2].split(" ")[4];
			JSONObject jo = new JSONObject();
			jo.put("modulus", modString);
			jo.put("public exponent", expString);
			System.out.println(jo);
		} catch (KeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
