package vaov.util.method;

import java.util.Arrays;
import java.util.Optional;

public class CommandExecuter {

	private Method[] methods;

	public CommandExecuter(Method... methods) {
		this.methods = methods;
	}

	public void execute(String[] args) {
		MethodParameters methodParameters = new MethodParameters(Arrays.asList(args));
		Optional<Method> methodOptional = Arrays.stream(methods).filter(method -> method.matches(methodParameters))
		.findAny();

		if (methodOptional.isPresent()) {
			methodOptional.get().run(methodParameters);
		} else {
			printHelpTexts();
		}
	}

	private void printHelpText(Method method) {
		method.helpText().stream().forEach(s -> System.out.println(s));
	}

	private void printHelpTexts() {
		Arrays.stream(methods).forEach(method -> printHelpText(method));
	}
}
