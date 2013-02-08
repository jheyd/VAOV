package de.piratenpartei.id.frontend.control;

import java.io.IOException;

public class TestWriter extends MyWriter {
	private String s="";
	private boolean lastMessageSentSuccessfully = true;

	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
		System.out.flush();
		lastMessageSentSuccessfully = true;
	}

	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		System.out.println(new String(arg0,arg1,arg2));
	}
	
	@Override
	public boolean wasLastMessageSentSuccessfully(){
		return lastMessageSentSuccessfully;
	}

}
