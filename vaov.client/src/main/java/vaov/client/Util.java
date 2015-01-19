package vaov.client;

public class Util {

	/**
	 * overwrites a char Array with zeroes
	 *
	 * @param c
	 *            the Array to overwrite
	 */
	public static void overwriteCharArray(char[] c) {
		for (int i = 0; i < c.length; i++ ) {
			c[i] = 0;
		}
	}

}
