package vaov.client.message.to;


public class VoteContentTO extends MessageContentTO {

	private Vote vote;

	public VoteContentTO(Vote vote) {
		this.vote = vote;
	}

	public Vote getVote() {
		return vote;
	}

}
