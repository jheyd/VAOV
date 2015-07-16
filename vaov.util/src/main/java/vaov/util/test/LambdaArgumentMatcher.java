package vaov.util.test;

import java.util.function.Predicate;

import org.mockito.ArgumentMatcher;

public class LambdaArgumentMatcher<T> extends ArgumentMatcher<T> {

	private Predicate<T> predicate;

	public LambdaArgumentMatcher(Predicate<T> predicate) {
		this.predicate = predicate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(Object argument) {
		return predicate.test((T) argument);
	}
}
