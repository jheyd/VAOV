package vaov.util.method;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MethodWithSubMethods extends Method {

	private List<SubMethod> subMethods;
	private Map<String, SubMethod> subMethodsByName;

	public MethodWithSubMethods(String command, List<SubMethod> subMethods) {
		super(command);
		this.subMethods = subMethods;

		this.subMethodsByName = getSubMethodsByName();
	}

	@Override
	public List<String> helpText() {
		return subMethods
		.stream()
		.map(
		subMethod -> {
			String paramsString = getCommand() + " " + subMethod.getSubCommand();

			paramsString += subMethod.getParams().stream().map(param -> "<" + param + ">")
			.collect(Collectors.joining(" "));

			return paramsString;
		}).collect(Collectors.toList());
	}

	@Override
	public boolean matches(MethodParameters parameters) {
		return hasSubMethod(parameters) && numberOfArgumentsMatches(parameters);
	}

	@Override
	public MethodResponse run(MethodParameters parameters) {
		return subMethodsByName.get(parameters.getSubCommand()).run(parameters);
	}

	private Map<String, SubMethod> getSubMethodsByName() {
		Map<String, SubMethod> subMethodsByName = new HashMap<>(subMethods.size());
		for (SubMethod subMethod : subMethods) {
			subMethodsByName.put(subMethod.getSubCommand(), subMethod);
		}
		return subMethodsByName;
	}

	private boolean hasSubMethod(MethodParameters parameters) {
		return subMethodsByName.containsKey(parameters.getSubCommand());
	}

	private boolean numberOfArgumentsMatches(MethodParameters parameters) {
		int argc = subMethodsByName.get(parameters.getSubCommand()).getArgc();
		return parameters.hasNumberOfParameters(argc);
	}

}
