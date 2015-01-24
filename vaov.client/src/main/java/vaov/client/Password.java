package vaov.client;

public class Password {

	public char[] getCharArray() {
		return charArray;
	}

	private char[] charArray;

	private boolean overwritten = false;

	public Password(char[] charArray) {
		this.charArray = charArray;
	}

	public void overwrite() {
		Util.overwriteCharArray(charArray);
		overwritten = true;
	}

	public boolean isOverwritten() {
		return overwritten;
	}

}
