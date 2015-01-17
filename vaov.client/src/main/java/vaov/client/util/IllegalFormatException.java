package vaov.client.util;

/**
 * This Exception is thrown if something failed while reading some message/file.
 * 
 * @author arne
 * 
 */
public class IllegalFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IllegalFormatException(String message) {
		super(message);
	}

	public IllegalFormatException(String message, Throwable t) {
		super(message, t);
	}
}
