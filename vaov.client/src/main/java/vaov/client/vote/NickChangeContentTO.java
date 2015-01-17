package vaov.client.vote;

public class NickChangeContentTO extends MessageContentTO {

	private String newNickName;

	public NickChangeContentTO(String newNickName) {
		this.newNickName = newNickName;
	}

	public String getNewNickName() {
		return newNickName;
	}

}
