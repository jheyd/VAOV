package de.piratenpartei.id.frontend;

public class WrongParameterCountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9053018589320996332L;

	public WrongParameterCountException() {
		new Exception();
	}
	
	public WrongParameterCountException(String message) {
		new Exception(message);
	}
}