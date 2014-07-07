package vaov.client.reduced.client;

public class WrongParameterCountException extends Exception {

	private int expected;
	private int actual;

	/**
	 * 
	 */
	private static final long serialVersionUID = -9053018589320996332L;

	public WrongParameterCountException(int expected, int actual) {
		super("Wrong parameter count! Expected: " + expected + ", Actual: "
				+ actual);
		this.expected = expected;
		this.actual = actual;
	}

	public int getActual() {
		return actual;
	}

	public int getExpected() {
		return expected;
	}
}