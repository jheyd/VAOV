package de.piratenpartei.id.frontend.topic;

import java.io.IOException;
import java.util.Arrays;

import de.piratenpartei.id.vote.IllegalFormatException;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.VerificationException;
import de.piratenpartei.id.vote.VoteManager;

/**
 * This class manages the proposal part of this application: downloading, uploading, showing, creating, editing propsals.
 * Dies ist ein Versuch, Abstimmungs- und Antragsanzeigetool voneinander zu trennen.
 * Diese Klasse ist  darauf ausgelegt, allein ausfühbar und mittels Texteingaben steuerbar zu sein.
 * 
 * @author artus
 *
 */

public class TopicManager {
	public static final String[] commandNames = new String[]{ "listTopics" , "showTopic" , "showIni" , "pull" };
	public static final String[] commandShortNames = new String[]{ "l" , "t" , "i" , "p" };

	public static void main(String[] args) {
		TopicManager tm = new TopicManager();
		if(args.length > 1){ // batch mode
			tm.execute(Arrays.copyOfRange(args, 1, args.length-1));
		} else { // interactive mode
			//TODO
		}		
	}

	private void execute(String[] args) {
		switch(Arrays.asList(commandNames).indexOf(args[0])){
		case(0):
			this.listTopics();
			break;
			
		case(1):
			this.showTopic(args[0]);
			break;
			
		case(2):
			this.showIni(args[0]);
			break;
		
		case(3):
			this.pull();
			break;
			
		default:
		}
	}

	private void listTopics() {
		// TODO Auto-generated method stub
		
	}
	private void showTopic(String topicID) {
		// TODO Auto-generated method stub
		
	}

	private void showIni(String targetID) {
		// TODO Auto-generated method stub
		
	}

	private void pull() {
		// TODO Auto-generated method stub
		
	}

}
