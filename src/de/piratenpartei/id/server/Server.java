package de.piratenpartei.id.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.piratenpartei.id.frontend.topic.Ini;
import de.piratenpartei.id.frontend.topic.TopicList;
import de.piratenpartei.id.vote.Account;
import de.piratenpartei.id.vote.KeyException;

public class Server {
	private TopicList topics;
	private List<Account> accounts;
	private ServerSocket serverSocket;
	private static final int PORT = 8020;

	public final static String defaultInPath = "bla";
	public final static String defaultOutPath = "foo";
	
	public static void main(String[] args) {
		Server server;
		server = new Server(Server.defaultInPath);
		server.run();
	}
	
	public Server(String path) {
		init(path);
	}
	
	private void init(String path) {
		try {
			this.buildTopicList(loadInis(path));
		} catch (IOException e) {
			System.out.println("Error reading Inis from file " + path);
			e.printStackTrace();
		}
		System.out.println("Building Topics finished");		

		try{
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Error while opening ServerSocket");
			e.printStackTrace();
		}
		System.out.println("ServerSocket opened at port " + PORT);
	}

	public void run() {
		boolean quit = false;
		String msg;
		while(!quit) {
            try{
                Socket server = serverSocket.accept();
                System.out.println("connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                msg = in.readUTF();
            	String returnMsg = action(msg);
            	out.writeUTF(returnMsg);
                server.close();
             }catch(IOException e){
                e.printStackTrace();
                break;
             } catch (KeyException e) {
            	System.out.println(e.getMessage());
				e.printStackTrace();
				break;
			}
        }
		storeInis(this.topics.toJSON(),Server.defaultOutPath);
	}

	private String action(String msg) throws KeyException {
		JSONObject jo = (JSONObject) JSONValue.parse(msg);
		if(!checkSignature(jo)) throw new KeyException("Signature not matching");
		String[] commands = new String[]{};
		String result = "";
		switch(Arrays.asList(commands).indexOf((String)jo.get("type"))){
		}
		return result;
	}

	private boolean checkSignature(JSONObject jo){
		return true;
	}
	
	public List<Account> loadAccounts(String path) throws IOException{
		JSONArray ja = (JSONArray) FileManager.load(path).get("accounts");
		List<Account> accs = new ArrayList<Account>();
		for(int i=0; i<ja.size(); i++){
			// TODO
			// accs.add(new Account((JSONObject) ja.get(i)));
		}
		return accs; 
	}
	
	private void storeAccounts(List<Account> accs, String path) {
		// TODO
		// FileManager.save(accs.toJSON, path);
	}

	private TopicList loadInis(String path) throws IOException{
		return new TopicList(FileManager.load(path));
	}
	
	private void storeInis(TopicList tl, String path) {
		FileManager.save(tl.toJSON(), path);
	}

	public void buildTopicList(JSONObject jo) throws IOException{
		System.out.println("Building Topics ...");

		String structure = (String) jo.get("structure");
		if(structure.equals("list"))
			topics = new TopicList((JSONObject) jo.get("data"));
		else
			throw new RuntimeException("Structure property in JSON-file has unknown value:" + structure);		
	}
	
	public void addIniInNewTopic(Ini ini, ArrayList<String> tags){
		this.topics.addIniInNewTopic(ini, tags);
	}
	
	public void addIniToTopic(Ini ini, int topicIndex){
		this.topics.addIniToTopic(ini, topicIndex);
	}
}
