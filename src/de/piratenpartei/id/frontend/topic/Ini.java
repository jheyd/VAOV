package de.piratenpartei.id.frontend.topic;


import org.json.simple.JSONObject;

public class Ini {
	private String caption;
	private String text;
	
	public Ini(String caption, String text){
		this.caption = caption;
		this.text = text;
	}
	
	public Ini(JSONObject jo) {
		this.fromJSON(jo);
	}

	public void fromJSON(JSONObject jo){
		this.caption = (String) jo.get("caption");
		this.text = (String) jo.get("text");
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("caption", this.caption);
		jo.put("text", this.text);
		return jo;
	}
	@Override
	public String toString(){
		return caption + ": " + text;
	}

	//Setters and Getters
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
