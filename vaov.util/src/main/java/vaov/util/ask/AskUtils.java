package vaov.util.ask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import vaov.util.password.Util;

public class AskUtils {

	/**
	 * Write a String to System.out, then read a String from System.in and read
	 * a boolean Value from it
	 *
	 * @param question
	 *            the String to write
	 * @return true if the read String is in {"Y","y",""}
	 */
	public static boolean askBool(String question) {
		String buf = askString(question + " [Y/n]");
		return buf.isEmpty() || buf.equals("y") || buf.equals("Y");
	}

	/**
	 * Write a String to System.out, then read a char[] from System.in
	 *
	 * @param question
	 *            the String to print
	 * @param bufLen
	 *            maximum length of the returned char array
	 * @return number of read characters
	 */
	public static void askCharArray(String question, char[] buf) {
		System.out.println(question);
		try (BufferedReader systemInReader = new BufferedReader(new InputStreamReader(System.in))) {
			systemInReader.read(buf);
			if (systemInReader.ready()) {
				System.out.println("input too long (max " + buf.length + " characters)");
				Util.overwriteCharArray(buf);
				askCharArray(question, buf);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Write a String to System.out, then write the Elements of a List<String>
	 * to System.out with leading number in the Format "(i+1) " + list[i]. Then
	 * read a String from System.in, convert it to int and subtract 1. This int
	 * is the index of the chosen element of possibleAnswers
	 *
	 * @param question
	 *            the String to write first
	 * @param possibleAnswers
	 *            the list to write to output with leading numbers
	 * @return the index of the chosen value
	 */
	public static Integer askIndexChoice(String question, List<String> possibleAnswers) {
		return askIndexChoice(question, possibleAnswers.toArray(new String[] {}));
	}

	/**
	 * Write a String to System.out, then write the Elements of a String[] to
	 * System.out with leading number in the Format "(i+1) " + list[i]. Then
	 * read a String vom System.in, convert it to int and subtract 1. This int
	 * is the index of the chosen element of possibleAnswers
	 *
	 * @param question
	 *            the String to write first
	 * @param possibleAnswers
	 *            the list to write to output with leading numbers
	 * @return the index of the chosen value
	 */
	public static Integer askIndexChoice(String question, String[] possibleAnswers) {
		Integer askInteger = askInteger(getIndexChoiceQuestion(question, possibleAnswers));
		if (askInteger == null) {
			return null;
		}
		return askInteger - 1;
	}

	/**
	 * Write a String to System.out, then read a String from System.in and
	 * convert it to int. If the read String is empty, read again.
	 *
	 * @param question
	 *            the String to write
	 * @return the last read line converted to int
	 */
	public static int askInt(String question) {
		Integer i;
		do {
			i = askInteger(question);
		} while (i == null);
		return i;
	}

	/**
	 * Write a String to System.out, then read a String from System.in and
	 * convert it to Integer
	 *
	 * @param question
	 *            the String to write
	 * @return the read line converted to Integer or null if the read String is
	 *         empty.
	 */
	public static Integer askInteger(String question) {
		String s = askString(question);
		if (s.isEmpty()) {
			return null;
		}
		return Integer.parseInt(s);
	}

	/**
	 * Write a String to System.out, then read a String from System.in
	 *
	 * @param question
	 *            the String to write
	 * @return the read line
	 */
	public static String askString(String question) {
		System.out.println(question);
		return scan();
	}

	/**
	 * Read a line from System.in
	 *
	 * @return the read line as a String
	 */
	public static String scan() {
		BufferedReader systemInReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			return systemInReader.readLine();
		} catch (Exception e) {
			throw new RuntimeException("read error: " + e.getMessage());
		}
	}

	private static String getIndexChoiceQuestion(String question, String[] possibleAnswers) {
		StringBuffer stringBuffer = new StringBuffer(question);
		for (int i = 0; i < possibleAnswers.length; i++ ) {
			stringBuffer.append("\n(" + String.valueOf(i + 1) + ") " + possibleAnswers[i]);
		}
		return stringBuffer.toString();
	}

}
