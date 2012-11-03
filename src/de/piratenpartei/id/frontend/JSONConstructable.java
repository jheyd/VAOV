package de.piratenpartei.id.frontend;

import org.json.simple.JSONObject;

/**
 * Interface for classes that can be JSON-serialized
 * @author dunkelzahn
 *
 */
public interface JSONConstructable {
	public void fromJSON(JSONObject jo);
	public JSONObject toJSON();
}
