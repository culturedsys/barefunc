package systems.cultured.barefunc.result;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static systems.cultured.barefunc.result.IsError.isError;
import static systems.cultured.barefunc.result.IsOk.isOk;

import org.junit.jupiter.api.Test;

public class ResultTest {
    @Test public void handleOfOkUsesOkHandler() {
        var ok = Result.ok(1);

        var handled = ok.handle(value -> "OK " + value, error -> "ERROR");

        assertThat(handled, equalTo("OK 1"));
    }

    @Test public void handleOfErrorUsesErrorHandler() {
        var error = Result.error(1);

        var handled = error.handle(value -> "OK " + value, e -> "ERROR " + e);

        assertThat(handled, equalTo("ERROR 1"));
    }


    @Test public void orElseOfOkIsValue() {
        var ok = Result.ok(1);

        var got = ok.orElse(2);

        assertThat(got, equalTo(1));
    }

    @Test public void orElseOfErrorIsDefault() {
        var error = Result.error(1);

        var got = error.orElse(2);

        assertThat(got, equalTo(2));
    }

    @Test public void mapOfOkIsMapped() {
        var ok = Result.ok(1);

        var mapped = ok.map(value -> value + 1);

        assertThat(mapped, isOk(equalTo(2)));
    }

    @Test public void mapOfErrorIsError() {
        var error = Result.<Integer, Integer>error(1);

        var mapped = error.map(value -> value + 1);

        assertThat(mapped, isError(equalTo(1)));
    }
}
