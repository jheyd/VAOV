package vaov.client;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Optional;
import java.util.function.Function;

import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import de.janheyd.javalibs.ask.AskUtils;
import de.janheyd.javalibs.method.Method;
import de.janheyd.javalibs.method.MethodParameters;
import de.janheyd.javalibs.method.MethodResponse;
import de.janheyd.javalibs.method.MethodWithoutSubMethods;

public class MethodFactory {

	private Control control;

	public MethodFactory(Control control) {
		super();
		this.control = control;
	}

	public MethodFactory() {
		this(new Control());
	}

	public MethodWithoutSubMethods createMessageMethod() {
		Function<MethodParameters, MethodResponse> function = parameters -> {
			Optional<PrivateAccount> askAcc = askAcc(parameters.getParameter(0));
			if (!askAcc.isPresent()) {
				return MethodResponse.error("Account not found");
			}
			control.message(askAcc.get(), parameters.getParameter(1), parameters.getParameter(2));
			return MethodResponse.success();
		};

		return new MethodWithoutSubMethods("message", function, "from", "to", "text");
	}

	public Method createNewAccountMethod(PrintWriter outputWriter) {
		Function<MethodParameters, MethodResponse> function = params -> {
			Password pass = askPassword("Enter password for the Account: ");
			String alias = control.newAccount(pass);
			outputWriter.println("Created new account: " + alias);
			return MethodResponse.success();
		};
		return new MethodWithoutSubMethods("newAccount", function);
	}

	public Method createRegisterAccountMethod() {
		Function<MethodParameters, MethodResponse> function = params -> MethodResponse.error("not yet implemented");
		return new MethodWithoutSubMethods("registerAccount", function);
	}

	public Method createVoteMethod() {
		Function<MethodParameters, MethodResponse> function = parameters -> {
			try {
				Optional<PrivateAccount> askAcc = askAcc(parameters.getParameter(0));
				if (!askAcc.isPresent()) {
					return MethodResponse.error("Account not found");
				}
				control.vote(askAcc.get(), parameters.getParameter(1), parameters.getParameter(2));
				return MethodResponse.success();
			} catch (ParseException e) {
				return MethodResponse.error(e.getMessage());
			}
		};

		return new MethodWithoutSubMethods("vote", function, "from", "targetId", "vote");
	}

	private Optional<PrivateAccount> askAcc(String alias) {
		Password password = askPassword("password for " + alias + ": ");
		Optional<PrivateAccount> acc = control.getAccount(alias, password);
		password.overwrite();
		return acc;
	}

	private static Password askPassword(String question) {
		char[] buf = new char[256];
		AskUtils.askCharArray(question, buf);
		return new Password(buf);
	}

}
