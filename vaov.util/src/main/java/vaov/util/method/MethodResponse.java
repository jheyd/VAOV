package vaov.util.method;

public class MethodResponse {

	private boolean successful;

	private String message;

	public static MethodResponse error(String message) {
		return new MethodResponse(message);
	}

	public static MethodResponse success() {
		return new MethodResponse();
	}

	private MethodResponse() {
		successful = true;
	}

	private MethodResponse(String message) {
		this.message = message;
		successful = false;
	}

	public String getMessage() {
		return message;
	}

	public boolean hasError() {
		return !successful;
	}

	public boolean isSuccessful() {
		return successful;
	}

}
