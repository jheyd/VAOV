package vaov.util.method;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MethodWithoutSubMethods extends Method {

	private List<String> params;
	private Function<MethodParameters, MethodResponse> function;

	public MethodWithoutSubMethods(String command, Function<MethodParameters, MethodResponse> function,
	String... params) {
		super(command);
		this.function = function;
		this.params = Arrays.asList(params);
	}

	@Override
	public List<String> helpText() {
		String helpText = getCommand();
		for (String param : params) {
			helpText += " <" + param + ">";
		}
		return Arrays.asList(helpText);
	}

	@Override
	public boolean matches(MethodParameters parameters) {
		return (parameters.hasNumberOfParameters(params.size()));
	}

	@Override
	public MethodResponse run(MethodParameters parameters) {
		return function.apply(parameters);
	}

}
