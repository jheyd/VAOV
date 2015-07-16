package vaov.util.password;

public class Password {

	private char[] charArray;

	private boolean overwritten = false;

	public Password(char[] charArray) {
		this.charArray = charArray;
	}

	public char[] getCharArray() {
		if (overwritten) {
			throw new RuntimeException("Password has already been overwritten.");
		}
		return charArray;
	}

	public boolean isOverwritten() {
		return overwritten;
	}

	public void overwrite() {
		for (int i = 0; i < charArray.length; i++ ) {
			charArray[i] = 0;
		}
		overwritten = true;
	}

}
