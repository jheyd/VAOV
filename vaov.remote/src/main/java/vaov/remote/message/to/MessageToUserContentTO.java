package vaov.remote.message.to;

public class MessageToUserContentTO extends MessageContentTO {

	private String username;
	private String message;

	public MessageToUserContentTO(String username, String message) {
		this.username = username;
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "message to user \"" + username + "\" with content\"" + message + "\"";
	}

}
