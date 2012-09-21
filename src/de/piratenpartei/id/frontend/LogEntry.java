package de.piratenpartei.id;

public class LogEntry implements Comparable{
	private int timeStamp;
	private String message;
	
	public LogEntry(String m, int t){
		this.timeStamp = t;
		this.message = m;
	}
	
	/**
	 * 
	 * @return timeStamp of the message in seconds since 1970-01-01 00:00:00 UTC
	 */
	public int getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * 
	 * @return the message text
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public int compareTo(Object arg0) {
		LogEntry arg = (LogEntry) arg0;
		if(this.timeStamp > arg.timeStamp) return 1;
		if(this.timeStamp == arg.timeStamp) return 0;
		if(this.timeStamp < arg.timeStamp) return -1;
		return 0;
	}
}
