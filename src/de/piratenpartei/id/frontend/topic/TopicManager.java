package de.piratenpartei.id.frontend.topic;

/**
 * This class manages the proposal part of this application: downloading, uploading, showing, creating, editing propsals.
 * Dies ist ein Versuch, Abstimmungs- und Antragsanzeigetool voneinander zu trennen.
 * Diese Klasse ist  darauf ausgelegt, allein ausfühbar und mittels Texteingaben steuerbar zu sein.
 * 
 * @author artus
 *
 */

public class TopicManager {
	public static final String[] commandNames = new String[]{ "message" , "vote" , "newIni" , "newTopic"};
	public static final String[] commandShortNames = new String[]{ "m" , "v" , "i" , "t" , "h" };
	public static final String paramDelimiter = ",";

	public static void main(String[] args) {
		
	}
}
