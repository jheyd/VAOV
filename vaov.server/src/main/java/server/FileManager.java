package de.piratenpartei.id.server;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class FileManager {
	public static final String key = "data";
	
	public static JSONObject load(String path){
		String temp = "";
		String s;
		try {
			java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path));
			while((s = br.readLine()) != null) temp += s;
			br.close();
		} catch (IOException e) {
			throw new RuntimeException("Error reading file \"" + path + "\": " + e.getMessage());
		}
		JSONObject jo = (JSONObject) JSONValue.parse(temp);
		return (JSONObject) jo.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public static void save(JSONObject obj, String path){
		JSONObject jo = new JSONObject();
		jo.put(key, obj);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(path));
			out.write(jo.toString());
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("Error writing to file \"" + path + "\": " + e.getMessage());
		}
	}
}
