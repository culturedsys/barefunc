package systems.cultured.barefunc.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;

public class IoTest {
    @Test
    public void pureRunsToValue() {
        assertThat(Io.pure(1).run(), equalTo(1));
    }

    @Test
    public void suspendRunsToValue() {
        assertThat(Io.suspend(() -> 1).run(), equalTo(1));
    }

    @Test
    public void flatMapRunsToValue() {
        assertThat(Io.pure(1).flatMap(i -> Io.pure(i + 1)).run(), equalTo(2));
    }
}
