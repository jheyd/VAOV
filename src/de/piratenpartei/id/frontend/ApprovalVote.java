package de.piratenpartei.id.frontend;


import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 * Implementation of the Approval voting procedure.
 * @author Dunkelzahn
 *
 */
public class ApprovalVote implements Vote {
	private boolean[] votes;
	public final static String voteTypeString = "approval";
	
	public ApprovalVote() {
	}

	public ApprovalVote(JSONObject jo) {
		this.fromJSON(jo);
	}

	public ApprovalVote(boolean[] votes) {
		this.votes = votes;
	}

	public boolean[] getVotes() {
		return votes;
	}

	public void setVotes(boolean[] votes) {
		this.votes = votes;
	}

	@Override
	public void fromJSON(JSONObject jo) {
		JSONArray ja = (JSONArray) jo.get("votes");
		this.votes = new boolean[ja.size()];
		for(int i=0; i<ja.size(); i++){
			this.votes[i] = (Boolean) ja.get(i);
		}
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for(int i=0; i<votes.length; i++) ja.add(votes[i]);
		jo.put("votes", ja);
		jo.put("type", ApprovalVote.voteTypeString);
		return jo;
	}

}
