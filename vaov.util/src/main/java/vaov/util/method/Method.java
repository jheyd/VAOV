package vaov.util.method;

import java.util.List;

public abstract class Method {

	private String command;

	public Method(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	public abstract List<String> helpText();

	public abstract boolean matches(MethodParameters parameters);

	public abstract MethodResponse run(MethodParameters parameters);

}
