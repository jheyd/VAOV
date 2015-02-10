package vaov.client.account.model;

import vaov.client.util.Util;

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
		Util.overwriteCharArray(charArray);
		overwritten = true;
	}

}
