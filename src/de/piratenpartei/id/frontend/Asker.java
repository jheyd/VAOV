package de.piratenpartei.id.frontend;
import java.util.List;


public class Asker {
	
	/**
	 * Read a line from System.in
	 * @return the read line
	 */
	public static String scan(){
		java.io.BufferedReader systemInReader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		try {
			return systemInReader.readLine();
		} catch(Exception e) {
			throw new RuntimeException("read error: " + e.getMessage());
		}
	}
	
	/**
	 * Write a String to System.out, then read a String from System.in
	 * @param question the String to write
	 * @return the read line
	 */
	public static String askString(String question){
		System.out.println(question);
		return scan();
	}
	
	/**
	 * Write a String to System.out, then read a String from System.in and convert it to Integer
	 * @param question the String to write
	 * @return the read line converted to Integer or null if the read String is empty.
	 */
	public static Integer askInteger(String question){
		String s = askString(question);
		if(s == "") return null;
		else return Integer.parseInt(s);
	}
	
	/**
	 * Write a String to System.out, then read a String from System.in and convert it to int. If the read String is empty, read again.
	 * @param question the String to write
	 * @return the last read line converted to int
	 */
	public static int askInt(String question){
		Integer i;
		while((i = askInteger(question)) == null); // if input String is empty, ask again
		return i;
	}
	
	/**
	 * Write a String to System.out, then write the Elements of a List<String> to System.out with leading number in the Format "(i+1) " + list[i].
	 * Then read a String vom System.in, convert it to int and subtract 1.
	 * This int is the index of the chosen element of possibleAnswers
	 * @param question the String to write first
	 * @param possibleAnswers the list to write to output with leading numbers
	 * @return the index of the chosen value
	 */
	public static Integer askIndexChoice(String question, List<String> possibleAnswers){
		return askIndexChoice(question, (String[]) possibleAnswers.toArray());
	}

	/**
	 * Write a String to System.out, then write the Elements of a String[] to System.out with leading number in the Format "(i+1) " + list[i].
	 * Then read a String vom System.in, convert it to int and subtract 1.
	 * This int is the index of the chosen element of possibleAnswers
	 * @param question the String to write first
	 * @param possibleAnswers the list to write to output with leading numbers
	 * @return the index of the chosen value
	 */
	public static Integer askIndexChoice(String question, String[] possibleAnswers){
		System.out.println(question);
		for(int i=0; i<possibleAnswers.length; i++)
			System.out.println("(" + String.valueOf(i+1) + ") " + possibleAnswers[i]);
		String s = scan();
		if(s == "") return null;
		else return Integer.valueOf(s) - 1;
	}
	
	/**
	 * Write a String to System.out, then read a String from System.in and read a boolean Value from it
	 * @param question the String to write
	 * @return true if the read String is in {"Y","y",""}
	 */
	public static boolean askBool(String question){
		System.out.println(question + " [Y/n]");
		String buf = scan();
		return buf == "y" || buf == "Y" || buf == "";
	}
	
	/**
	 * Write a String to System.out, then read a char[] from System.in
	 * @param question the String to write
	 * @return the read line
	 */
	public static char[] askCharArray(String question){
		System.out.println(question);
		java.io.BufferedReader systemInReader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		char[] buf = new char[256];
		try {
			systemInReader.read(buf);
		} catch(Exception e) {
			throw new RuntimeException("read error: " + e.getMessage());
		}
		return buf;
	}

}
