package de.piratenpartei.id.frontend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.piratenpartei.id.frontend.model.topic.Topic;

public class IniModel {
	private List<Topic> topics;
	private List<String> tags;
	
	public void refreshTags(){
		this.tags = new ArrayList<String>();
		for(int i=0; i<topics.size(); i++){
			List<String> tagList = topics.get(i).getTags();
			for(int k=0; k<tagList.size(); k++){
				String thisTag = tagList.get(k);
				if(!tags.contains(thisTag)){
					tags.add(thisTag);
				}
			}
		}
		Collections.sort(tags);
	}
	
	public IniModel(List<Topic> topicList) {
		topics = topicList;
		refreshTags();
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public List<String> getTags() {
		return tags;
	}
}
