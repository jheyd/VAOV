package vaov.client.message.to;


/**
 * 
 * Implementation of a paper ballot where each box on the ballot is represented
 * by a boolean value For a simple Yes/No vote the values should be {true,false}
 * (for "Yes"), {false,true} (for "No"), or {false,false} (for "Abstain")
 * 
 * @author Dunkelzahn
 * 
 */
public class VoteTO {

	private boolean[] votes;
	private String targetID;

	/**
	 * Votes should always be constructed
	 * 
	 * @param votes
	 * @param target
	 */
	public VoteTO(boolean[] votes, String target) {
		this.votes = votes;
		targetID = target;
	}

	public String getTargetID() {
		return targetID;
	}

	public boolean[] getVotes() {
		return votes;
	}

}
