package de.piratenpartei.id.frontend.control;

public class WrongParameterCountException extends Exception {

	private int expected;
	private int actual;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9053018589320996332L;

	public WrongParameterCountException(int expected, int actual) {
		this.expected = expected;
		this.actual = actual;
		new Exception();
	}
	
	public int getExpected() {
		return expected;
	}

	public int getActual() {
		return actual;
	}
}