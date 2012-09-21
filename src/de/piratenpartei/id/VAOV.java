package de.piratenpartei.id;

import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.*;

/**
 * 
 * This class ist the top-level interface that should be used by GUIs. Data should be processed using only this class.
 * @author Dunkelzahn
 *
 */
public class VAOV {
	private TopicList tops;
	private List<Ini> inis;
	private Messenger m;
	private MessageLog messages;
	
	/**
	 * 
	 * @throws IOException 
	 */
	public VAOV(Account a, String path) throws IOException {
		this.inis = new ArrayList<Ini>();
		this.buildTopicList(loadInis(path));
		System.out.println("Building Topics finished");
		
		m = new Messenger(a); // TODO
	}

	public VAOV(Account a) {
		this.inis = new ArrayList<Ini>();
		this.tops = new TopicList();

		m = new Messenger(a); // TODO
	}
	
	public void vote(Topic topic, Vote vote, String type) throws IOException, IllegalFormatException, KeyException, VerificationException{
		m.sendVote(this.tops.getTopics().indexOf(topic), vote, type);
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
		TextStore ts = new TextStore(data);

		String structure = (String) ts.get("structure");
		if(structure.equals("list")){
			tops = new TopicList((JSONObject) ts.get("data"));
			for(int i=0; i<tops.getTopics().size(); i++){
				ArrayList<Ini> inis = this.tops.getTopics().get(i).getInis();
				for(int k=0; k<inis.size(); k++){
					this.inis.add(inis.get(k));
				}
			}
		}
		else throw new RuntimeException("Structure property in JOSN-file has unknown value:" + structure);		
	}
	
	public void addIniInNewTopic(Ini ini, ArrayList<String> tags){
		this.tops.addIniInNewTopic(ini, tags);
		this.inis.add(ini);
	}
	
	public void addIniToTopic(Ini ini, int topicIndex){
		this.tops.addIniToTopic(ini, topicIndex);
		this.inis.add(ini);
	}
	
	public int getIndexOf(Ini ini){
		return this.inis.indexOf(ini);
	}
	
	public Topic getTopicOf(Ini ini){
		Topic result = null;
		for(int i=0; i<tops.getTopics().size(); i++){
			if(tops.getTopics().get(i).getInis().indexOf(ini) != -1){
				result = tops.getTopics().get(i);
				i = tops.getTopics().size();
			}
		}
		return result;
	}
	
	public String loadInis(String path) throws IOException{
		BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path));

		String s;
		String result = "";
		while((s = br.readLine()) != null) result += s;
		br.close();
		
		return result;
	}
	
}
