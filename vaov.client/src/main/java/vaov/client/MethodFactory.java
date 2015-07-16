package vaov.client;

import java.io.PrintWriter;
import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.util.Optional;
import java.util.function.Function;

import vaov.client.account.model.PrivateAccount;
import vaov.client.service.RemoteServiceFactory;
import vaov.util.ask.AskUtils;
import vaov.util.method.Method;
import vaov.util.method.MethodParameters;
import vaov.util.method.MethodResponse;
import vaov.util.method.MethodWithoutSubMethods;
import vaov.util.password.Password;

public class MethodFactory {

	private Control control;

	private static Password askPassword(String question) {
		char[] buf = new char[256];
		AskUtils.askCharArray(question, buf);
		return new Password(buf);
	}

	public MethodFactory(Control control) {
		super();
		this.control = control;
	}

	public MethodFactory(RemoteServiceFactory remoteServiceFactoryImpl) {
		this(new Control(remoteServiceFactoryImpl));
	}

	public MethodWithoutSubMethods createMessageMethod() {
		Function<MethodParameters, MethodResponse> function = parameters -> {
			Optional<PrivateAccount> askAcc;
			try {
				askAcc = askAcc(parameters.getParameter(0));
			} catch (UnrecoverableKeyException e) {
				return MethodResponse.error("Could not load key. Wrong Password? Reason: " + e.getMessage());
			}
			if (!askAcc.isPresent())
				return MethodResponse.error("Account not found");
			control.message(askAcc.get(), parameters.getParameter(1), parameters.getParameter(2));
			return MethodResponse.success();
		};

		return new MethodWithoutSubMethods("message", function, "from", "to", "text");
	}

	public Method createNewAccountMethod(PrintWriter outputWriter) {
		Function<MethodParameters, MethodResponse> function = params -> {
			Password pass = askPassword("Enter password for the Account: ");
			String alias;
			try {
				alias = control.newAccount(pass);
			} catch (UnrecoverableKeyException e) {
				return MethodResponse.error("Could not store key. Wrong Password? Reason: " + e.getMessage());
			}
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
			Optional<PrivateAccount> askAcc;
			try {
				askAcc = askAcc(parameters.getParameter(0));
			} catch (UnrecoverableKeyException e) {
				return MethodResponse.error("Could not load key. Wrong Password? Reason: " + e.getMessage());
			}
			if (!askAcc.isPresent())
				return MethodResponse.error("Account not found");
			try {
				control.vote(askAcc.get(), parameters.getParameter(1), parameters.getParameter(2));
				return MethodResponse.success();
			} catch (ParseException e) {
				return MethodResponse.error(e.getMessage());
			}
		};

		return new MethodWithoutSubMethods("vote", function, "from", "targetId", "vote");
	}

	private Optional<PrivateAccount> askAcc(String alias) throws UnrecoverableKeyException {
		Password password = askPassword("password for " + alias + ": ");
		Optional<PrivateAccount> acc = control.getAccount(alias, password);
		password.overwrite();
		return acc;
	}

}
