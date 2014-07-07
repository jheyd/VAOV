package vaov.client.frontend.control;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;

public class MyWriter extends Writer {
	private String s;
	private boolean lastMessageSentSuccessfully = true;
	public static String SERVERNAME = "127.0.0.1";
	public static int PORT = 8090;

	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
		// TODO set lastMessageSentSuccessfully: true if successful, false
		// otherwise
		Socket client = new Socket(SERVERNAME, PORT);
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		out.writeUTF(s);
		client.close();
	}

	public boolean wasLastMessageSentSuccessfully() {
		return lastMessageSentSuccessfully;
	}

	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		s += arg0;
	}

}
