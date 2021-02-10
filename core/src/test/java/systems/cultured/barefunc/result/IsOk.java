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

public class IsOk<T> extends TypeSafeMatcher<Result<T, ?>> {
    private Matcher<? super T> matcher;

	public static <T> IsOk<T> isOk(Matcher<? super T> matcher) {
        return new IsOk<>(matcher);
    }

    public IsOk(Matcher<? super T> matcher) {
		this.matcher = matcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Ok result containing ").appendDescriptionOf(matcher);        
    }

    @Override
    protected boolean matchesSafely(Result<T, ?> item) {
        return item.handle(matcher::matches, error -> false);
    }

}
