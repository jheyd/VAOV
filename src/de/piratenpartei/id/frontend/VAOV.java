package de.piratenpartei.id.frontend;


import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
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
 * 
 * This class ist the top-level interface that should be used by GUIs. Data should be processed using only this class.
 * @author Dunkelzahn
 *
 */
public class VAOV {
	private TopicList tops;
	private Messenger m;
	private MessageLog messages;
	
	/**
	 * 
	 * @throws IOException 
	 */
	public VAOV(Account a, String path) throws IOException {
		this.buildTopicList(loadInis(path));
		System.out.println("Building Topics finished");
		
		m = new Messenger(a);
	}

	public VAOV(Account a) {
		this.tops = new TopicList();

		m = new Messenger(a);
	}
	
	public VAOV(boolean test, Account a){
		if(test) test(a);
		else this.tops = new TopicList();

		m = new Messenger(a);
	}
	
	
	public void vote(Topic topic, Vote vote) throws IOException, IllegalFormatException, KeyException, VerificationException{
		m.sendVote(this.tops.getTopics().indexOf(topic), vote);
	}
	
	/**
	 * Pull data (new messages, new TopicList, notifications) from server
	 * should be run at application start
	 */
	public void pull(){
		Hashtable hashes = pullHashes();
		if((Integer)hashes.get("topicList") == this.tops.hashCode()) this.tops = (TopicList) pullData("topicList");
		if((Integer)hashes.get("messages") == this.messages.hashCode()) this.messages = (MessageLog) pullData("messages");
	}
	
	private Object pullData(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	private Hashtable pullHashes() {
		// TODO Auto-generated method stub
		return null;
	}

	private int[] localHashes() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Push data (Messages, new Inis, new Topics) to server.
	 * There should be run two methods to use it:
	 * 1) automatically after each action that creates new data
	 * 2) manually by the user (esp, for low bandwith connections)
	 */
	public void push(){
		
	}
	
	public void buildTopicList(String data) throws IOException{
		System.out.println("Building Topics ...");
		JSONStore ts = new JSONStore(data);

		String structure = (String) ts.get("structure");
		if(structure.equals("list"))
			tops = new TopicList((JSONObject) ts.get("data"));
		
		else throw new RuntimeException("Structure property in JOSN-file has unknown value:" + structure);		
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
}
