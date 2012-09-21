package de.piratenpartei.id;

import java.util.List;

public class CSVStore implements Store {
	private String store;
	private List<Character> delimiters;
	public static final char[] defaultDelimiters = new char[]{
		';' , ','
	};
	
	public CSVStore() {
		
	}
	
	@Override
	public void put(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(String key, Store value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromString(String s) {
		// TODO Auto-generated method stub

	}

}
