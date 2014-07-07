package vaov.client.reduced.client.writers;

import java.io.IOException;

public class MessageWriterTestingImpl extends MessageWriter {

	private boolean lastMessageSentSuccessfully = true;
	StringBuffer content = new StringBuffer();

	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
	}

	public String getContent() {
		return content.toString();
	}

	@Override
	public boolean wasLastMessageSentSuccessfully() {
		return lastMessageSentSuccessfully;
	}

	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		content.append(new String(arg0, arg1, arg2));
	}

}
