package vaov.util.method;

import java.util.List;

public class MethodParameters {

	private List<String> args;

	public MethodParameters(List<String> args) {
		this.args = args;
	}

	public String getParameter(int index) {
		return args.get(index + 1);
	}

	public String getSubCommand() {
		return args.get(0);
	}

	public boolean hasNumberOfParameters(int count) {
		return args.size() == count + 1;
	}

}