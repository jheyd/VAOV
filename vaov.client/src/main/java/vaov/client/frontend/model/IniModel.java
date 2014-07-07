package vaov.client.frontend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vaov.client.frontend.model.topic.Topic;

public class IniModel {
	private List<Topic> topics;
	private List<String> tags;

	public IniModel(List<Topic> topicList) {
		topics = topicList;
		refreshTags();
	}

	public List<String> getTags() {
		return tags;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void refreshTags() {
		this.tags = new ArrayList<String>();
		for (int i = 0; i < topics.size(); i++) {
			List<String> tagList = topics.get(i).getTags();
			for (int k = 0; k < tagList.size(); k++) {
				String thisTag = tagList.get(k);
				if (!tags.contains(thisTag))
					tags.add(thisTag);
			}
		}
		Collections.sort(tags);
	}
}
