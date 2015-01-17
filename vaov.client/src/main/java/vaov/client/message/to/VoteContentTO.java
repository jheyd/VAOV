package vaov.client.message.to;


public class VoteContentTO extends MessageContentTO {

	private VoteTO vote;

	public VoteContentTO(VoteTO vote) {
		this.vote = vote;
	}

	public VoteTO getVote() {
		return vote;
	}

}
