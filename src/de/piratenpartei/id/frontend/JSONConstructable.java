package de.piratenpartei.id.frontend;

import org.json.simple.JSONObject;

public interface JSONConstructable {
	public void fromJSON(JSONObject jo);
	public JSONObject toJSON();
}
