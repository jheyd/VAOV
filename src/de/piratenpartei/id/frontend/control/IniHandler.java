package de.piratenpartei.id.frontend.control;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.piratenpartei.id.frontend.model.IniModel;
import de.piratenpartei.id.frontend.model.topic.Topic;
import de.piratenpartei.id.vote.IllegalFormatException;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;
import de.piratenpartei.id.vote.VerificationException;

public class IniHandler {

	private IniModel model;
	public static final String topicListFilePath = "topics.dat";
	
	public IniHandler() {
		pull();

		JSONObject jo = Util.loadJSONDataFromFile(topicListFilePath);
		JSONObject jo_data = (JSONObject) jo.get("data");
		
		List<Topic> topics = new ArrayList<Topic>();
		JSONArray ja_topics = (JSONArray) jo_data.get("list");
		for(int i=0; i<ja_topics.size(); i++)
			topics.add(new Topic((JSONObject) ja_topics.get(i)));
		
		model = new IniModel(topics);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject InisToJSON(){
		JSONArray ja_topics = new JSONArray();
		for(int i=0; i<model.getTopics().size(); i++)
			ja_topics.add(model.getTopics().get(i).toJSON());
		
		JSONArray ja_tags = new JSONArray();
		for(int i=0; i<model.getTags().size(); i++)
			ja_tags.add(model.getTags().get(i));
		
		JSONObject jo = new JSONObject();
		jo.put("topics", ja_topics);
		jo.put("tags", ja_tags);
		return jo;
	}
	
	/**
	 * creates a new Ini
	 * @param topicID the ID of the Topic to create the Ini in or "new" to create the Ini in a new Topic
	 * @param name the name / title of the Ini to create
	 * @param text the text of the Ini
	 * @throws ParseException 
	 */
	public void newIni(PrivateAccount acc, String topicID, String name, String text) throws ParseException{
		// check if topicID has valid format
		String toParse;
		String[] validBegins = new String[]{"new","NEW","top","TOP"};
		if(Arrays.asList(validBegins).contains(topicID.substring(0, 3)))
			toParse = topicID.substring(0, 3);
		else
			toParse = topicID;
		try { Integer.parseInt(toParse); }
		catch(NumberFormatException e) { throw new ParseException("topicID has invalid format", 0); }
		
		try { MessageHandler.sendNewIni(name, text, topicID, acc); }
		catch (IllegalFormatException e)	{ e.printStackTrace(); }
		catch (KeyException e)				{ e.printStackTrace(); }
		catch (VerificationException e)		{ e.printStackTrace(); }
	}	

	public List<String> listTopics() {
		List<String> l = new ArrayList<String>();
		List<Topic> t = model.getTopics();
		for(int i=0; i<t.size(); i++) l.add("TOP" + String.valueOf(i) + ": " + t.get(i).getIniNames().get(0));
		return l;
	}
	
	
	/**
	 * 
	 * @param topicID ID of the Topic to show. Format: "TOP" + index
	 * @throws ParseException 
	 */
	public List<String> getTopicInfo(String topicID) throws ParseException {
		if(topicID.length() < 4){
			throw new ParseException("", 0);
		}
		if(!topicID.substring(0, 3).equals("TOP")){
			throw new ParseException("", 0);
		}
		try{
			int index = Integer.parseInt(topicID.substring(3));
			List<String> tags = model.getTopics().get(index).getTags();
			String s="";
			for(int i=0; i<tags.size(); i++) s += tags.get(i) + ", ";
			
			List<String> result = new ArrayList<String>();
			result.add(s.substring(0,s.length()-2));
			
			result.addAll(model.getTopics().get(index).getIniNames());
			return result;
		} catch (NumberFormatException e){
			throw new ParseException("", 3);
		}
	}
	
	
	/**
	 * 
	 * @param targetID ID of the Ini to show, Format: "INI" + topicIndex + "." + iniIndex
	 * @throws ParseException 
	 */
	public String getIni(String targetID) throws ParseException{
		if(targetID.length() < 6){
			throw new ParseException("", 0);
		}
		if(!targetID.substring(0, 3).equals("INI"))
			throw new ParseException("", 0);
		String[] indices = targetID.substring(3).split("\\.");
		if(indices.length != 2)
			throw new ParseException("", 3);			
		int index1 = 0;
		int index2 = 0;
		try{
			index1 = Integer.parseInt(indices[0]);
		} catch (NumberFormatException e){
			throw new ParseException("", 3);
		}
		try{
			index2 = Integer.parseInt(indices[1]);
		} catch (NumberFormatException e){
			throw new ParseException("", indices[0].length() + 3);
		}
		return model.getTopics().get(index1).getInis().get(index2).toString();
	}

	
	/**
	 * load TopicList data from server and write them to the file at topicListFilePath
	 */
	public void pull() {
		// TODO Auto-generated method stub
	}
	

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
}
