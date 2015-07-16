package vaov.util.method;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class SubMethod {

	private String subCommand;
	private List<String> params;
	private Function<MethodParameters, MethodResponse> function;

	public SubMethod(String subCommand, Function<MethodParameters, MethodResponse> function, String... params) {
		this.subCommand = subCommand;
		this.function = function;
		this.params = Arrays.asList(params);
	}

	public int getArgc() {
		return params.size();
	}

	public List<String> getParams() {
		return params;
	}

	public String getSubCommand() {
		return subCommand;
	}

	public MethodResponse run(MethodParameters parameters) {
		return function.apply(parameters);
	}

}
