package de.piratenpartei.id.frontend.cui;
import java.util.List;


public class Asker {

	public static String scan(){
		java.io.BufferedReader systemInReader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		try {
			return systemInReader.readLine();
		} catch(Exception e) {
			throw new RuntimeException("read error: " + e.getMessage());
		}
	}
	public static String askString(String question){
		System.out.println(question);
		return scan();
	}
	public static Integer askInteger(String question){
		String s = askString(question);
		if(s == null) return null;
		else return Integer.parseInt(s);
	}
	public static int askInt(String question){
		String s;
		while((s = askString(question)) == null);
		return Integer.parseInt(s);
	}
	public static Integer askIndexChoice(String question, List<String> possibleAnswers){
		return askIndexChoice(question, (String[]) possibleAnswers.toArray());
	}
	public static Integer askIndexChoice(String question, String[] possibleAnswers){
		System.out.println(question);
		for(int i=0; i<possibleAnswers.length; i++)
			System.out.println("(" + String.valueOf(i+1) + ") " + possibleAnswers[i]);
		String s = scan();
		if(s == "") return null;
		else return Integer.valueOf(s) - 1;
	}
	public static boolean askBool(String question){
		System.out.println(question + " [Y/n]");
		String buf = scan();
		return buf == "y" || buf == "Y" || buf == "";
	}

}
