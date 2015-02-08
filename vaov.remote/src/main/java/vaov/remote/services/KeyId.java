package vaov.remote.services;

public class KeyId {

	private static final String ACCOUNT_ALIAS_PRIVATE = "_privatekey";
	private static final String ACCOUNT_ALIAS_PUBLIC = "_publickey";

	private String alias;

	public KeyId(String alias) {
		this.alias = alias;
	}

	public String getPrivateAlias() {
		return alias + ACCOUNT_ALIAS_PRIVATE;
	}

	public String getPublicAlias() {
		return alias + ACCOUNT_ALIAS_PUBLIC;
	}

	public String getAlias() {
		return alias;
	}

	@Override
	public String toString() {
		return alias;
	}
}