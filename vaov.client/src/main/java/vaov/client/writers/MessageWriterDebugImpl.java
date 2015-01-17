package vaov.client.writers;

import java.io.IOException;

public class MessageWriterDebugImpl extends MessageWriter {

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
	public boolean wasLastMessageSentSuccessfully() {
		return lastMessageSentSuccessfully;
	}

	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		System.out.println(new String(arg0, arg1, arg2));
	}

}
