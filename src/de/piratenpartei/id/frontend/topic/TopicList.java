package de.piratenpartei.id.frontend.topic;


import java.util.*;

import org.json.simple.*;

import de.piratenpartei.id.frontend.JSONConstructable;

/**
 * 
 * @author dunkelzahn
 *
 */
public class TopicList implements JSONConstructable {
	
	/* 
	 * The Categories from Liquid Feedback are implemented here as "tags".
	 * Categories are a special case of tags: every topic can only have one tag.
	 */
	private ArrayList<String> tags;
	private ArrayList<Topic> topics;

	public TopicList(){
		this.init();
	}
	
	public TopicList(JSONObject jo) {
		this.fromJSON(jo);
	}

	private void init() {
		this.topics = new ArrayList<Topic>();
		this.refreshTags();
	}

	/**
	 * read tags from the Elements of this.topics and write them to this.tags
	 */
	private void refreshTags(){
		this.tags = new ArrayList<String>();
		for(int i=0; i<topics.size(); i++){
			ArrayList<String> tagList = topics.get(i).getTags();
			for(int k=0; k<tagList.size(); k++){
				String thisTag = tagList.get(k);
				if(!tags.contains(thisTag)){
					tags.add(thisTag);
				}
			}
		}
		Collections.sort(tags);
	}
	
	/**
	 * create a new Topic and add an Ini to it
	 * @param ini: the first Ini in the topic
	 */
	public void addIniInNewTopic(Ini ini, List<String> tags){
		Topic t = new Topic();
		t.addIni(ini);
		for(int i=0; i<tags.size(); i++){
			t.addTag(tags.get(i));
		}
		this.topics.add(t);
		this.refreshTags();
	}
	
	/**
	 * Add an Ini to an existing Topic
	 * @param ini: the Ini to add
	 * @param TopicIndex: the index of the topic to add the Ini to
	 */
	public void addIniToTopic(Ini ini, int TopicIndex){
		this.topics.get(TopicIndex).addIni(ini);
		this.refreshTags();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(){
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for(int i=0; i<this.topics.size(); i++){
			ja.add(this.topics.get(i).toJSON());
		}
		jo.put("list", ja);
		return jo;
	}
	
	@Override
	public void fromJSON(JSONObject jo){
		this.init();
		JSONArray arr = (JSONArray) jo.get("list");
		for(int i=0; i<arr.size(); i++){
			this.topics.add(new Topic((JSONObject)arr.get(i)));
		}
		this.refreshTags();
	}
	
	public static TopicList parseJSONString(JSONObject jo){
		return new TopicList(jo);
	}

	// default Setters and Getters
	public ArrayList<String> getCategories(){
		return this.tags;
	}
	
	public ArrayList<Topic> getTopics(){
		return this.topics;
	}
	
}
