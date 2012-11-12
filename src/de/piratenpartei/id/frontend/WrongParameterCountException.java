package de.piratenpartei.id.frontend;

public class WrongParameterCountException extends Exception {

	public WrongParameterCountException() {
		new Exception();
	}
	
	public WrongParameterCountException(String message) {
		new Exception(message);
	}
}