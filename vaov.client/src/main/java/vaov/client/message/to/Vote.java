package vaov.client.message.to;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import vaov.client.JSONConstructable;

/**
 * 
 * Implementation of a paper ballot where each box on the ballot is represented
 * by a boolean value For a simple Yes/No vote the values should be {true,false}
 * (for "Yes"), {false,true} (for "No"), or {false,false} (for "Abstain")
 * 
 * @author Dunkelzahn
 * 
 */
public class Vote implements JSONConstructable {

	private boolean[] votes;
	private String targetID;

	/**
	 * Votes should always be constructed
	 * 
	 * @param votes
	 * @param target
	 */
	public Vote(boolean[] votes, String target) {
		this.votes = votes;
		targetID = target;
	}

	public Vote(JSONObject jo) {
		fromJSON(jo);
	}

	@Override
	public void fromJSON(JSONObject jo) {
		JSONArray ja = (JSONArray) jo.get("votes");
		votes = new boolean[ja.size()];
		for (int i = 0; i < ja.size(); i++)
			votes[i] = (Boolean) ja.get(i);
		targetID = (String) jo.get("targetID");
	}

	public String getTargetID() {
		return targetID;
	}

	public boolean[] getVotes() {
		return votes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for (boolean vote : votes)
			ja.add(vote);
		jo.put("votes", ja);
		jo.put("targetID", targetID);
		return jo.toJSONString();
	}

}
