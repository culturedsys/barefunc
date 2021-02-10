/* 
 * Copyright 2021 Tim Fisken
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 */

package systems.cultured.barefunc.result;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsError<E> extends TypeSafeMatcher<Result<?, E>> {
    public static <E> IsError<E> isError(Matcher<? super E> matcher) {
        return new IsError<>(matcher);
    }

    private Matcher<? super E> matcher;

    public IsError(Matcher<? super E> matcher) {
        this.matcher = matcher;
    }

	@Override
	public void describeTo(Description description) {
        description.appendText("Error result containing ").appendDescriptionOf(matcher);
	}

	@Override
	protected boolean matchesSafely(Result<?, E> item) {
        return item.handle(value -> false, error -> matcher.matches(error));
	}
}
