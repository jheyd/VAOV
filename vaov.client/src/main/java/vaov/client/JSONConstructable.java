package vaov.client;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Interface for classes that can be JSON-serialized
 * 
 * @author dunkelzahn
 * 
 */
public interface JSONConstructable extends JSONAware {

	public void fromJSON(JSONObject jo);
}
