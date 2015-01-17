package vaov.client.vote;

public class MessageToUserContentTO extends MessageContentTO {

	private String username;
	private String message;

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
