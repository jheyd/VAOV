package de.piratenpartei.id.frontend;


import org.json.simple.*;

public class JSONStore implements Store {
	JSONObject store;
	
	public JSONStore() {
		store = new JSONObject();
	}
	
	public JSONStore(String data) {
		this.fromString(data);
	}

	@Override
	public void put(String key, Object value){
		store.put(key, value);
	}
	
	@Override
	public Object get(String key){
		return store.get(key);
	}
	
	@Override
	public void fromString(String s) {
		store = (JSONObject) JSONValue.parse(s);
	}

	@Override
	public String toString(){
		return store.toJSONString();
	}

	@Override
	public void put(String key, Store value) {
		store.put(key, value);
	}
	
}
