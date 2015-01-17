package vaov.client.writers;

import java.io.Writer;

abstract public class MessageWriter extends Writer {

	abstract public boolean wasLastMessageSentSuccessfully();

}
