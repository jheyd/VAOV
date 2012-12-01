package de.piratenpartei.id.frontend;


import java.io.BufferedReader;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.*;

import de.piratenpartei.id.frontend.topic.Ini;
import de.piratenpartei.id.frontend.topic.Topic;
import de.piratenpartei.id.frontend.topic.TopicList;
import de.piratenpartei.id.vote.Account;
import de.piratenpartei.id.vote.IllegalFormatException;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.Messenger;
import de.piratenpartei.id.vote.VerificationException;


/**
 * outdated
 * This class ist the top-level interface that should be used by GUIs. Data should be processed using only this class.
 * @author Dunkelzahn
 *
 */
@Deprecated
public class VAOV {
	private TopicList tops;
	private Account account;
	
	/**
	 * 
	 * @throws IOException 
	 */
	public VAOV(Account a, String path) throws IOException {
		this.buildTopicList(loadInis(path));
		System.out.println("Building Topics finished");
		
		this.account = a;
		this.init();
	}

	public VAOV(Account a) {
		this.tops = new TopicList();
		
		this.account = a;
		this.init();
	}
	
	public VAOV(boolean test, Account a){
		if(test) test(a);
		else this.tops = new TopicList();
		
		this.account = a;
		this.init();
	}
	
	public void init(){
	}
	
	public void vote(Topic topic, Vote vote) throws IOException, IllegalFormatException, KeyException, VerificationException{
		Messenger.sendVote(vote,account);
	}
	
	public void buildTopicList(String data) throws IOException{
		System.out.println("Building Topics ...");
		JSONObject jo = (JSONObject) JSONValue.parse(data);

		String structure = (String) jo.get("structure");
		if(structure.equals("list"))
			tops = new TopicList((JSONObject) jo.get("data"));
		else
			throw new RuntimeException("Structure property in JSON-file has unknown value:" + structure);		
	}
	
	public void addIniInNewTopic(Ini ini, ArrayList<String> tags){
		this.tops.addIniInNewTopic(ini, tags);
	}
	
	public void addIniToTopic(Ini ini, int topicIndex){
		this.tops.addIniToTopic(ini, topicIndex);
	}
	
	public String loadInis(String path) throws IOException{
		BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path));

		String s;
		String result = "";
		while((s = br.readLine()) != null) result += s;
		br.close();
		
		return result;
	}
	
	public void test(Account a){
		List<String> tags = Arrays.asList(new String[]{"Gartengestaltung"});
		
		this.tops = new TopicList();
		this.tops.addIniInNewTopic(new Ini("Blumen sind schön","Ich finde, dass Blumen schön sind"), tags);
		this.tops.addIniToTopic(new Ini("Blumen machen Arbeit","Ich finde, dass Blumen zu viel Arbeit machen"), 0);
		System.out.println("Building TopicList for testing finished");
	}
	public Topic getTestTopic(){
		return tops.getTopics().get(0);
	}

	public List<Topic> getTopics() {
		return this.tops.getTopics();
	}
}
