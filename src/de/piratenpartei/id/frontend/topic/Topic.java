package de.piratenpartei.id.frontend.topic;


import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Topic {
	private ArrayList<String> tags;
	private ArrayList<Ini> inis;
	
	public Topic(){
		this.init();
	}

	public Topic(JSONObject jo) {
		this.fromJSON(jo);
	}

	public void init(){
		this.tags = new ArrayList<String>();
		this.inis = new ArrayList<Ini>();
	}
	
	/**
	 * map "getName()" to all elements of "inis" and return the return values as a List<String>
	 */
	public List<String> getIniNames() {
		List<String> result = new ArrayList<String>();
		for(int i=0; i<inis.size(); i++) result.add(this.inis.get(i).getCaption());
		return result;
	}

	public void addTag(String tag){
		this.tags.add(tag);
	}
	
	public void addIni(Ini ini){
		this.inis.add(ini);
	}
	
	@SuppressWarnings("unchecked")
	public void fromJSON(JSONObject jo){
		this.init();

		JSONArray inisArr = (JSONArray) jo.get("inis");
		for(int i=0; i<inisArr.size(); i++){
			inis.add(new Ini((JSONObject)inisArr.get(i)));
		}
		JSONArray tagsArr = (JSONArray) jo.get("tags");
		tags.addAll(tagsArr);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		JSONArray inisArr = new JSONArray();
		for(int i=0; i<this.inis.size(); i++){
			inisArr.add(this.inis.get(i).toJSON());
		}
		JSONArray tagsArray = new JSONArray();
		for(int i=0; i<this.tags.size(); i++){
			tagsArray.add(this.tags.get(i));
		}
		jo.put("inis", inisArr);
		jo.put("tags", tagsArray);
		return jo;
	}

	// Setters and Getters
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public ArrayList<Ini> getInis() {
		return inis;
	}
	public void setInis(ArrayList<Ini> inis) {
		this.inis = inis;
	}
}
