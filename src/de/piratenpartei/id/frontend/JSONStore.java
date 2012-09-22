package de.piratenpartei.id.frontend;


import org.json.simple.*;

public class JSONStore {
	JSONObject store;
	
	public JSONStore() {
		store = new JSONObject();
	}
	
	public JSONStore(String data) {
		this.fromString(data);
	}

	public void put(String key, Object value){
		store.put(key, value);
	}
	
	public Object get(String key){
		return store.get(key);
	}
	
	public void fromString(String s) {
		store = (JSONObject) JSONValue.parse(s);
	}

	public String toString(){
		return store.toJSONString();
	}

}
