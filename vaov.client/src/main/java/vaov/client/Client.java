package vaov.client;

import java.io.PrintWriter;

import vaov.util.method.CommandExecuter;
import vaov.util.method.Method;

/**
 * API for the Client
 *
 * @author dunkelzahn
 *
 */
public class Client {

	private PrintWriter outputWriter = new PrintWriter(System.out, true);

	private CommandExecuter commandExecuter;

	public static void main(String[] args) {
		Client client = new Client();
		client.setOutput(new PrintWriter(System.out));
		client.execute(args);
	}

	public Client() {
		MethodFactory methodFactory = new MethodFactory();
		Method messageMethod = methodFactory.createMessageMethod();
		Method voteMethod = methodFactory.createVoteMethod();
		Method newAccountMethod = methodFactory.createNewAccountMethod(outputWriter);
		Method registerAccountMethod = methodFactory.createRegisterAccountMethod();

		commandExecuter = new CommandExecuter(messageMethod, voteMethod, newAccountMethod, registerAccountMethod);
	}

	public void execute(String[] args) {
		commandExecuter.execute(args);
	}

	void setOutput(PrintWriter output) {
		outputWriter = output;
	}

}
