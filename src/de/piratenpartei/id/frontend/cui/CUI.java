package de.piratenpartei.id.frontend.cui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.piratenpartei.id.frontend.VAOV;
import de.piratenpartei.id.frontend.topic.Topic;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

public class CUI {
	private VAOV v;
	
	public static void main(String[] args) {
		CUI cui = new CUI();
		cui.run(args);
	}

	private void run(String[] args) {
		try { v = new VAOV(new PrivateAccount()); }
		catch (KeyException e) { e.printStackTrace(); }
		
		if(args.length >= 1) execute(args);
		else while(execute(args));
	}

	private boolean execute(String[] args) {
		List<String> possibleOperators = new ArrayList<String>();
		String[] ops = new String[]{
				"list_topics",
				"list_inis",
				"show_ini",
				"vote",
				"quit"};
		
		switch(Arrays.asList(ops).indexOf(args[1])){
		case(0):
			List<Topic> l = this.v.getTopics();
			for(int i=0; i<l.size(); i++){
				Topic top = l.get(i);
				String line = "topic " + top.getID();
				line += " inis: ";
				for(int k=0; k<top.getInis().size(); k++){
					line += top.getInis().get(k).getID() + " \"" + top.getInis().get(k).getCaption() + "\", ";
				}
				System.out.println(line.substring(0, line.length()-3)); // remove ", " at end of line
			}
			break; 
		case(1):
			break;
		case(2):
			break;
		case(3):
			return false;
		default: throw new RuntimeException("Unknown operator!");
		}
		return true;
	}
}
