package de.piratenpartei.id.test;

import static org.junit.Assert.*;

import org.json.simple.*;
import org.junit.Test;

public class TestJSON {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		
		JSONObject jo1 = new JSONObject();
		jo1.put("foo", "bar");
		String s = (String) jo1.get("foo");
		assertEquals("bar", s);
		System.out.println(s);

	}

}
