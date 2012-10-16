package de.piratenpartei.id.frontend;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 * Implementation of the Approval voting procedure.
 * @author Dunkelzahn
 *
 */
public class Vote implements JSONConstructable {
	private boolean[] votes;
	private String targetID;
	
	
	public Vote() {
	}

	public Vote(boolean[] votes, String target) {
		this.votes = votes;
		this.targetID = target;
	}

	public Vote(JSONObject jo) {
		this.fromJSON(jo);
	}

	@Override
	public void fromJSON(JSONObject jo) {
		JSONArray ja = (JSONArray) jo.get("votes");
		this.votes = new boolean[ja.size()];
		for(int i=0; i<ja.size(); i++){
			this.votes[i] = (Boolean) ja.get(i);
		}
		this.targetID = (String) jo.get("targetID");
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for(int i=0; i<votes.length; i++) ja.add(votes[i]);
		jo.put("votes", ja);
		jo.put("targetID", this.targetID);
		return jo;
	}

	public boolean[] getVotes() {
		return votes;
	}

	public void setVotes(boolean[] votes) {
		this.votes = votes;
	}

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

}
