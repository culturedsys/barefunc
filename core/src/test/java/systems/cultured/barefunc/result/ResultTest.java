package systems.cultured.barefunc.result;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test public void orElseGetOfOkIsValue() {
        var ok = Result.ok(1);

        var got = ok.orElseGet(error -> 2);

        assertThat(got, equalTo(1));
    }

    @Test public void orElseGetOfErrorIsMappedError() {
        var ok = Result.error(1);

        var got = ok.orElseGet(error -> error + 1);

        assertThat(got, equalTo(2));
    }

    @Test public void orElseThrowOfOkIsValue() {
        var ok = Result.ok(1);

        var got = ok.orElseThrow(error -> new RuntimeException("Error"));

        assertThat(got, equalTo(1));
    }

    @Test public void orElseThrowOfErrorThrowsRuntimeException() {
        var error = Result.error(1);

        var thrown = 
                assertThrows(RuntimeException.class, () -> error.orElseThrow(e -> new RuntimeException(Integer.toString(e))));

        assertThat(thrown.getMessage(), equalTo("1"));
    }

    @Test public void orElseThrowOfErrorThrowsException() {
        var error = Result.error(1);

        var thrown = assertThrows(Exception.class, () -> error.orElseThrow(e -> new Exception(Integer.toString(e))));

        assertThat(thrown.getMessage(), equalTo("1"));
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
