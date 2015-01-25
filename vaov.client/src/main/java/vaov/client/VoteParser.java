package vaov.client;

import static java.lang.String.valueOf;

import java.text.ParseException;

public abstract class VoteParser {

	public static boolean[] parseVoteString(String voteString) throws ParseException {
		boolean[] votes = new boolean[voteString.length()];
		for (int i = 0; i < voteString.length(); i++ ) {
			votes[i] = VoteParser.parseVote(voteString, i);
		}
		return votes;
	}

	private static boolean parseVote(String voteString, int index) throws ParseException {
		String singleVoteString = valueOf(voteString.charAt(index));
		if (YES_SYMBOLS.contains(singleVoteString)) {
			return true;
		}
		if (NO_SYMBOLS.contains(singleVoteString)) {
			return false;
		}

		throw new ParseException("voteString contains invalid characters.", index);
	}

	private static final String NO_SYMBOLS = "nNfF0";
	private static final String YES_SYMBOLS = "yYjJtT1";

}
