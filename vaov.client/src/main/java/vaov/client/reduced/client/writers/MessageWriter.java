package vaov.client.reduced.client.writers;

import java.io.Writer;

abstract public class MessageWriter extends Writer {

	abstract public boolean wasLastMessageSentSuccessfully();

}
