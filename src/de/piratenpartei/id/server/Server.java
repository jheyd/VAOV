package de.piratenpartei.id.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.piratenpartei.id.frontend.model.topic.Ini;
import de.piratenpartei.id.frontend.model.topic.TopicList;
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
			this.loadInis(path);
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
		storeInis(this.topics,Server.defaultOutPath);
	}

	private String action(String msg) throws KeyException {
		JSONObject jo = (JSONObject) JSONValue.parse(msg);
		if(!checkSignature(jo))
			throw new KeyException("Signature not matching");
		String result = "";
		JSONObject data;
		switch((String)jo.get("type")){
		case("newIni"):
			data = (JSONObject) jo.get("data");
			String s_idNum = ((String)data.get("targetID")).substring(3);
			int idNum=0;
			try{
				idNum = Integer.parseInt(s_idNum);
			} catch (Exception e){
				return "malformed targetID";
			}
			this.topics.addIniToTopic(new Ini((String) data.get("caption"), (String) data.get("text")), idNum);
			result="Ini created in Topic TOP" + idNum + "with ID INI" + String.valueOf(this.topics.getTopics().get(idNum).getInis().size()-1);
			break;
		case("newTopic"):
			data = (JSONObject) jo.get("data");
			JSONArray ja_tags = (JSONArray) data.get("tags");
			List<String> tags = new ArrayList<String>();
			for(int i=0; i<ja_tags.size(); i++)
				tags.add((String) ja_tags.get(i));
			this.topics.addIniInNewTopic(new Ini((String) data.get("caption"), (String) data.get("text")), tags);
			result="New Topic created as TOP" + String.valueOf(this.topics.getTopics().size()-1);
			break;
		case("deleteIni"):
			result="";
			break;
		case("changeIni"):
			result="";
			break;
		case("vote"):
			result="";
			break;
		case("messageToUser"):
			result="";
			break;
		case("nickChange"):
			result="";
			break;
		case("pullTopicList"):
			result=this.topics.toJSON().toJSONString();
			break;
		case("pullUserMessages"):
			result="";
			break;
		case("newAccount"):
			// TODO
			//accounts.add(new Account());
			result="";
			break;
		case("revokeAccount"):
			result="";
			break;
		default:
			result = "unknown Command";
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
			// accs.adJSONObjectd(new Account((JSONObject) ja.get(i)));
		}
		return accs; 
	}
	
	private void storeAccounts(List<Account> accs, String path) {
		JSONArray ja = new JSONArray();
		for(int i=0; i<accs.size(); i++)
			// TODO
			ja.add(accs.get(i)/*.toJSON*/);
		JSONObject jo = new JSONObject();
		jo.put("accounts", ja);
		FileManager.save(jo, path);
	}

	private TopicList loadInis(String path) throws IOException{
		return new TopicList(FileManager.load(path));
	}
	
	private void storeInis(TopicList tl, String path) {
		FileManager.save(tl.toJSON(), path);
	}

	public void addIniInNewTopic(Ini ini, List<String> tags){
		this.topics.addIniInNewTopic(ini, tags);
	}
	
	public void addIniToTopic(Ini ini, int topicIndex){
		this.topics.addIniToTopic(ini, topicIndex);
	}
}
