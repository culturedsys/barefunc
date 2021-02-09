package systems.cultured.barefunc.flatteners;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static systems.cultured.barefunc.result.IsError.isError;
import static systems.cultured.barefunc.result.IsOk.isOk;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;

import systems.cultured.barefunc.result.Result;

public class FlattenersTest {
    @Test public void flatteningEmptyStreamIsOkEmptyList() {
        var result = Stream.<Result<Object, Object>>empty().collect(Flatteners.result());

        assertThat(result, isOk(IsEmptyCollection.empty()));
    }

    @Test public void flatteningStreamOfOkIsOkList() {
        var result = Stream.of(Result.ok(1), Result.ok(2)).collect(Flatteners.result());

        assertThat(result, isOk(contains(1, 2)));
    }

    @Test public void flatteningStreamOfErrorIsError() {
        var result = Stream.<Result<Integer, Integer>>of(Result.ok(1), Result.error(2)).collect(Flatteners.result());

        assertThat(result, isError(equalTo(2)));
    }

    @Test public void flatteningStreamOfErrorsCollectsErrors() {
        var result = Stream.<Result<Integer, Integer>>of(Result.error(1), Result.error(2))
                .collect(Flatteners.result(Collectors.toList(), Collectors.toList()));

        assertThat(result, isError(contains(1, 2)));
    }
}
