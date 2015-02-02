package vaov.client;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Optional;
import java.util.function.Function;

import vaov.client.account.model.Password;
import vaov.client.account.model.PrivateAccount;
import vaov.client.util.Util;
import de.janheyd.javalibs.method.Method;
import de.janheyd.javalibs.method.MethodParameters;
import de.janheyd.javalibs.method.MethodResponse;
import de.janheyd.javalibs.method.MethodWithoutSubMethods;

public class MethodFactory {

	public static MethodWithoutSubMethods createMessageMethod() {
		Function<MethodParameters, MethodResponse> function = parameters -> {
			Optional<PrivateAccount> askAcc = Util.askAcc(parameters.getParameter(0));
			if (!askAcc.isPresent()) {
				return MethodResponse.error("Account not found");
			}
			Control.message(askAcc.get(), parameters.getParameter(1), parameters.getParameter(2));
			return MethodResponse.success();
		};

		return new MethodWithoutSubMethods("message", function, "from", "to", "text");
	}

	public static Method createNewAccountMethod(PrintWriter outputWriter) {
		Function<MethodParameters, MethodResponse> function = params -> {
			Password pass = Util.askPassword("Enter password for the Account: ");
			String alias = Control.newAccount(pass);
			outputWriter.println("Created new account: " + alias);
			return MethodResponse.success();
		};
		return new MethodWithoutSubMethods("newAccount", function);
	}

	public static Method createRegisterAccountMethod() {
		Function<MethodParameters, MethodResponse> function = params -> MethodResponse.error("not yet implemented");
		return new MethodWithoutSubMethods("registerAccount", function);
	}

	public static Method createVoteMethod() {
		Function<MethodParameters, MethodResponse> function = parameters -> {
			try {
				Optional<PrivateAccount> askAcc = Util.askAcc(parameters.getParameter(0));
				if (!askAcc.isPresent()) {
					return MethodResponse.error("Account not found");
				}
				Control.vote(askAcc.get(), parameters.getParameter(1), parameters.getParameter(2));
				return MethodResponse.success();
			} catch (ParseException e) {
				return MethodResponse.error(e.getMessage());
			}
		};

		return new MethodWithoutSubMethods("vote", function, "from", "targetId", "vote");
	}

}
