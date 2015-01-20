package vaov.client;

import java.io.PrintWriter;

import de.janheyd.javalibs.method.CommandExecuter;
import de.janheyd.javalibs.method.Method;

/**
 * API for the Client
 *
 * @author dunkelzahn
 *
 */
public class Client {

	public static void main(String[] args) {
		Client client = new Client();
		client.setOutput(new PrintWriter(System.out));
		client.execute(args);
	}

	private PrintWriter outputWriter;

	private CommandExecuter commandExecuter;

	public Client() {
		Method messageMethod = MethodFactory.createMessageMethod();
		Method voteMethod = MethodFactory.createVoteMethod();
		Method newAccountMethod = MethodFactory.createNewAccountMethod(outputWriter);
		Method registerAccountMethod = MethodFactory.createRegisterAccountMethod();

		commandExecuter = new CommandExecuter(messageMethod, voteMethod, newAccountMethod, registerAccountMethod);
	}

	public void execute(String[] args) {
		commandExecuter.execute(args);
	}

	void setOutput(PrintWriter output) {
		outputWriter = output;
	}

}
