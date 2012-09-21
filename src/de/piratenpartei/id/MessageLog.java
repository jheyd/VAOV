package de.piratenpartei.id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageLog {
	private List<LogEntry> l;
	
	public void addEntry(String message, int timeStamp){
		LogEntry le = new LogEntry(message, timeStamp);
		Collections.sort(l);
	}
	
	public List<LogEntry> getFrom(int from){
		List<LogEntry> result = new ArrayList<LogEntry>();
		for(int i=0; i<l.size(); i++){
			if(this.l.get(i).getTimeStamp() >= from) result.add(this.l.get(i)); 
		}
		return result;
	}
	
	public List<LogEntry> getFromTo(int from, int to){
		List<LogEntry> result = new ArrayList<LogEntry>();
		for(int i=0; i<l.size(); i++){
			LogEntry e = l.get(i);
			if(e.getTimeStamp() >= from && e.getTimeStamp() <= to)
				result.add(e); 
		}
		return result;
	}
	
	public LogEntry getEntry(int i){
		return getL().get(i);
	}

	// default set / get
	public List<LogEntry> getL() {
		return l;
	}

	public void setL(List<LogEntry> l) {
		this.l = l;
	}
}
