package vaov.client;

import java.io.PrintWriter;
import java.text.ParseException;

import vaov.client.vote.KeyException;
import vaov.client.vote.PrivateAccount;

/**
 * API for the Client
 *
 * @author dunkelzahn
 *
 */
public class Client {

	public static void main(String[] args) {
		Client cli = new Client();
		cli.setOutput(new PrintWriter(System.out));
		if (debugCommand.length > 0)
			args = debugCommand;
		if (args.length > 0)
			cli.execute(args);
		else {
			boolean cont = true;
			while (cont)
				cont = cli.execute(Util.askString("#: ").split(" "));
		}
	}

	public static final String[] debugCommand = {};

	private static String[] HELPTEXTS = { "message <from> <to> <text>", "vote <from> <targetID> <vote>", "newAccount",
			"registerAccount <key>", "exit" };

	private PrintWriter outputWriter = new PrintWriter(System.out);

	private void printHelpText(String string) {
		System.out.println("usage: ");
		for (String helpText : HELPTEXTS)
			if (helpText.startsWith(string))
				System.out.println(helpText);
	}

	private void printHelpTexts() {
		System.out.println("usage: ");
		for (String helpText : HELPTEXTS)
			System.out.println(helpText);

	}

	PrintWriter getOutput() {
		return outputWriter;
	}

	void setOutput(PrintWriter output) {
		outputWriter = output;
	}

	/**
	 * Get PrivateAccount associated with a username form the KeyStore, asking
	 * for the password on the command line.
	 *
	 * @param username
	 * @return
	 * @throws KeyException
	 */
	public PrivateAccount askAcc(String username) throws KeyException {
		char[] pass = Util.askCharArray("password for " + username + ": ");
		PrivateAccount acc = Control.getAccount(username, pass);
		if (pass != null)
			for (int i = 0; i < pass.length; i++ )
				pass[i] = 'a'; // overwrite password in memory
		return acc;
	}

	/**
	 * Execute a command in String format commands: message <from> <to> <text>,
	 * vote <from> <targetID> <vote>, newAccount, registerAccount <key>
	 *
	 * @param args
	 *            command to execute and parameters
	 * @return false if args[0] == "quit"
	 */
	public boolean execute(String[] args) {

		if (args.length == 0) {
			printHelpTexts();
			return true;
		} else {
			switch (args[0]) {
			case "message": // message
				if (args.length != 4)
					printHelpText(args[0]);
				else
					try {
						Control.message(askAcc(args[1]), args[2], args[3]);
					} catch (KeyException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				break;
			case "vote": // vote
				if (args.length != 4)
					printHelpText(args[0]);
				else
					try {
						Control.vote(askAcc(args[1]), args[2], args[3]);
					} catch (ParseException e) {
						outputWriter.println("Error while Parsing voteString at voteString["
						+ String.valueOf(e.getErrorOffset()) + "]: " + e.getMessage());
					} catch (KeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				break;
			case "newAccount": // newAccount
				if (args.length != 1)
					printHelpText(args[0]);
				else {
					String username = Util.askString("Enter username for the Account: ");
					char[] pass = Util.askCharArray("Enter password for the Account: ");
					try {
						Control.newAccount(username, pass);
					} catch (KeyException e) {
						outputWriter.println("KeyException in function \"newAccount\": " + e.getMessage());
						e.printStackTrace();
					}
				}
				break;
			case "registerAccount": // registerAccount
				outputWriter.println("not yet implemented");
				// registerAccount();
				break;
			case "exit":
				return false;
			default:
				printHelpTexts();
			}
			outputWriter.flush();
			return true;
		}
	}

}
