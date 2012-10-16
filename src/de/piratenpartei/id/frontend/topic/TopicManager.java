package de.piratenpartei.id.frontend.topic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.piratenpartei.id.frontend.Asker;

/**
 * This class manages the proposal part of this application: downloading, uploading, showing, creating, editing propsals.
 * Dies ist ein Versuch, Abstimmungs- und Antragsanzeigetool voneinander zu trennen.
 * Diese Klasse ist  darauf ausgelegt, allein ausfühbar und mittels Texteingaben steuerbar zu sein.
 * 
 * @author dunkelzahn
 *
 */
public class TopicManager {
	public static final String[] commandNames = new String[]{ "listTopics" , "showTopic" , "showIni" , "pull" };
	public static final String[] commandShortNames = new String[]{ "l" , "t" , "i" , "p" };
	public static final String topicListFilePath = "topics.dat";
	
	private TopicList tops;

	public static void main(String[] args) {
		TopicManager tm = new TopicManager();
		if(args.length > 1){ // batch mode
			tm.execute(Arrays.copyOfRange(args, 1, args.length-1));
		} else { // interactive mode
			String s = Asker.askString("Enter the Command to execute: ");
			tm.execute(s.split(" "));
		}		
	}

	private void execute(String[] args) {
		switch(Arrays.asList(commandNames).indexOf(args[0])){
		case(0): this.listTopics();			break;
		case(1): this.showTopic(args[1]);	break;
		case(2): this.showIni(args[1]);		break;
		case(3): this.pull();				break;
		}
	}

	private void listTopics() {
		this.buildTopicList();
		List<Topic> t = tops.getTopics();
		for(int i=0; i<t.size(); i++) System.out.println("TOP" + String.valueOf(i) + ": " + t.get(i).toString());
	}

	/**
	 * 
	 * @param topicID ID of the Topic to show. Format: "TOP" + index
	 */
	private void showTopic(String topicID) {
		this.buildTopicList();
		int index = Integer.parseInt(topicID.substring(3));
		System.out.println(this.tops.getTopics().get(index).toString());
	}
	
	/**
	 * 
	 * @param targetID ID of the Ini to show, Format: "INI" + topicIndex + "." + iniIndex
	 */
	private void showIni(String targetID) {
		this.buildTopicList();
		String[] indices = targetID.substring(3).split("\\.");
		int index1 = Integer.parseInt(indices[0]);
		int index2 = Integer.parseInt(indices[1]);
		System.out.println(this.tops.getTopics().get(index1).getInis().get(index2).toString());
	}

	/**
	 * load TopicList data from server and write them to the file at topicListFilePath
	 */
	private void pull() {
		// TODO Auto-generated method stub
	}
	
	private void buildTopicList(){
		BufferedReader br;
		String s = "";
		String buf = "";
		try {
			br = new java.io.BufferedReader(new java.io.FileReader(TopicManager.topicListFilePath));
			while((buf = br.readLine()) != null) s += buf;
			br.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		JSONObject jo = (JSONObject) JSONValue.parse(s);
		this.tops = new TopicList((JSONObject) jo.get("data"));
	}
	
}
