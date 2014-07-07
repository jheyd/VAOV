package vaov.client.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import vaov.client.frontend.control.MessageHandler;
import vaov.client.frontend.model.Vote;
import vaov.client.frontend.model.topic.Ini;
import vaov.client.frontend.model.topic.Topic;
import vaov.client.frontend.model.topic.TopicList;
import vaov.client.vote.Account;
import vaov.client.vote.IllegalFormatException;
import vaov.client.vote.KeyException;
import vaov.client.vote.VerificationException;

/**
 * outdated This class ist the top-level interface that should be used by GUIs.
 * Data should be processed using only this class.
 * 
 * @author Dunkelzahn
 * 
 */
@Deprecated
public class VAOV {
	private TopicList tops;
	private Account account;

	public VAOV(Account a) {
		this.tops = new TopicList();

		this.account = a;
		this.init();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public VAOV(Account a, String path) throws IOException {
		this.buildTopicList(loadInis(path));
		System.out.println("Building Topics finished");

		this.account = a;
		this.init();
	}

	public VAOV(boolean test, Account a) {
		if (test)
			test(a);
		else
			this.tops = new TopicList();

		this.account = a;
		this.init();
	}

	public void addIniInNewTopic(Ini ini, ArrayList<String> tags) {
		this.tops.addIniInNewTopic(ini, tags);
	}

	public void addIniToTopic(Ini ini, int topicIndex) {
		this.tops.addIniToTopic(ini, topicIndex);
	}

	public void buildTopicList(String data) throws IOException {
		System.out.println("Building Topics ...");
		JSONObject jo = (JSONObject) JSONValue.parse(data);

		String structure = (String) jo.get("structure");
		if (structure.equals("list"))
			tops = new TopicList((JSONObject) jo.get("data"));
		else
			throw new RuntimeException(
					"Structure property in JSON-file has unknown value:"
							+ structure);
	}

	public Topic getTestTopic() {
		return tops.getTopics().get(0);
	}

	public List<Topic> getTopics() {
		return this.tops.getTopics();
	}

	public void init() {
	}

	public String loadInis(String path) throws IOException {
		BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(
				path));

		String s;
		String result = "";
		while ((s = br.readLine()) != null)
			result += s;
		br.close();

		return result;
	}

	public void test(Account a) {
		List<String> tags = Arrays.asList(new String[] { "Gartengestaltung" });

		this.tops = new TopicList();
		this.tops.addIniInNewTopic(new Ini("Blumen sind schön",
				"Ich finde, dass Blumen schön sind"), tags);
		this.tops.addIniToTopic(new Ini("Blumen machen Arbeit",
				"Ich finde, dass Blumen zu viel Arbeit machen"), 0);
		System.out.println("Building TopicList for testing finished");
	}

	public void vote(Topic topic, Vote vote) throws IOException,
			IllegalFormatException, KeyException, VerificationException {
		MessageHandler.sendVote(vote, account);
	}
}
