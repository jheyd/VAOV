package de.piratenpartei.id.frontend;

import org.json.simple.JSONObject;

public class YesNoVote implements Vote {
	private boolean vote;
	private boolean abstain;
	public final static String voteTypeString = "yesNo";
	
	public YesNoVote() {
	}
	public YesNoVote(JSONObject jo) {
		this.fromJSON(jo);
	}
	public YesNoVote(boolean vote, boolean abstain) {
		this.vote = vote;
		this.abstain = abstain;
	}
	
	public boolean isVote() {
		return vote;
	}
	
	public void setVote(boolean vote) {
		this.vote = vote;
	}
	public boolean isAbstained() {
		return abstain;
	}
	public void setAbstained(boolean abstained) {
		this.abstain = abstained;
	}
	@Override
	public void fromJSON(JSONObject jo) {
		this.abstain = (Boolean) jo.get("abstain");
		this.vote = (Boolean) jo.get("vote");
	}
	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("abstain", abstain);
		jo.put("vote", vote);
		jo.put("type", YesNoVote.voteTypeString);
		return jo;
	}
	
}
