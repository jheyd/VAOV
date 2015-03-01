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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		KeyId other = (KeyId) obj;
		if (alias == null) {
			if (other.alias != null) {
				return false;
			}
		} else if (!alias.equals(other.alias)) {
			return false;
		}
		return true;
	}
}