package de.piratenpartei.id.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.piratenpartei.id.frontend.topic.Ini;
import de.piratenpartei.id.frontend.topic.TopicList;
import de.piratenpartei.id.vote.Account;

public class Server {
	private TopicList topics;
	private List<Account> accounts;
	private ServerSocket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ServerSocketChannel ssc;
	private Selector select;private static final int PORT = 8020;


	public final static String defaultInPath = "bla";
	public final static String defaultOutPath = "foo";
	
	public static void main(String[] args) {
		Server server;
		server = new Server(Server.defaultInPath);
		server.run();
		server.storeInis(Server.defaultOutPath);
	}
	
	private void storeInis(String path) {
		// TODO Auto-generated method stub
		
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

		try {
			select = Selector.open();
			ssc = ServerSocketChannel.open();
        	ssc.configureBlocking(false);
        	ssc.socket().bind(new InetSocketAddress(PORT));
        	ssc.register(select, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			System.out.println("Error while opening ServerSocket");
			e.printStackTrace();
		}

		
	}

	public void run() {
		boolean quit = false;
        while(!quit) {
        	try {
        		if(select.select() <= 0)
        			quit = true;
        	} catch(IOException e) {
        		System.out.println("The select broke!");
        		e.printStackTrace();
        		quit = true;
        		break;
        	}
        	String msg = this.getMessage();
        	action(msg);
        }
	}

	private void action(String msg) {
		JSONObject jo = (JSONObject) JSONValue.parse(msg);
		String[] commands = new String[]{};
		switch(Arrays.asList(commands).indexOf((String)jo.get("type"))){
		}
	}

	private String getMessage(){
		String msg="";
        Iterator<SelectionKey> iter = select.keys().iterator();
		while(iter.hasNext()){
			SelectionKey key = iter.next();
    		if(key.isAcceptable()) {
    			// System.out.println("accept");
    			try {
    				SocketChannel sockChan = ssc.accept();
    				sockChan.configureBlocking(false);
    				sockChan.register(select, SelectionKey.OP_READ);
    			} catch(IOException e) {
    				System.out.println("Error while accepting socket");
    				e.printStackTrace();
    			}
    		} else if(key.isReadable()) {
    			// System.out.println("read");
    			SocketChannel client = (SocketChannel)key.channel();
    			
    			ByteBuffer buf = ByteBuffer.allocate(1024);
    			try {
    				
    				int readBytes = -1;
   					readBytes = client.read(buf);
					if(readBytes < 0) {
						// Client closed connection
						// System.out.println("client closed");
						client.close();
					} else {
						buf.flip();
						msg = decodeBuf(buf).toString().trim();
					}
					} catch (IOException e) {
						System.out.println("Error while receiving data from client");
						e.printStackTrace();
					}


    		}
		}
		return msg;
	}

	public static CharBuffer decodeBuf(ByteBuffer buf) {
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer charBuffer = null;
		try {
			charBuffer = decoder.decode(buf);
		} catch (CharacterCodingException e) {
			System.out.println("Error: Could not decode ByteBuffer");
		}
		
		return charBuffer;
	}
	
	public String loadInis(String path) throws IOException{
		BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path));

		String s;
		String result = "";
		while((s = br.readLine()) != null) result += s;
		br.close();
		
		return result;
	}
	
	public void buildTopicList(String data) throws IOException{
		System.out.println("Building Topics ...");
		JSONObject jo = (JSONObject) JSONValue.parse(data);

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
