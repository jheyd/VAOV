package vaov.client;

public class Password {

	private char[] charArray;

	private boolean overwritten = false;

	public Password(char[] charArray) {
		this.charArray = charArray;
	}

	public char[] getCharArray() {
		return charArray;
	}

	public boolean isOverwritten() {
		return overwritten;
	}

	public void overwrite() {
		Util.overwriteCharArray(charArray);
		overwritten = true;
	}

}
