package de.piratenpartei.id.frontend;


import org.json.simple.*;

public class JSONStore {
	JSONObject jo;
	
	public JSONStore() {
		jo = new JSONObject();
	}
	
	public JSONStore(String data) {
		this.fromString(data);
	}

	public void put(String key, Object value){
		jo.put(key, value);
	}
	
	public Object get(String key){
		return jo.get(key);
	}
	
	public void fromString(String s) {
		jo = (JSONObject) JSONValue.parse(s);
	}

	public String toString(){
		return jo.toJSONString();
	}

}
