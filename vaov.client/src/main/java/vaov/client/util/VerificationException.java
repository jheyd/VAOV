package vaov.client.util;

/**
 * This exception is thrown, if a message failed to verify.
 *
 * @author arne
 *
 */
public class VerificationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VerificationException(String message) {
		super(message);
	}

}
