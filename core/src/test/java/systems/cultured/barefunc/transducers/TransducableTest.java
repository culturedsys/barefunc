package systems.cultured.barefunc.transducers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class TransducableTest {
  @Test
  void canTransduceStream() {
    List<Integer> result = Transducable.transduceStream(Stream.of(1, 2, 3), Transducer.map(i -> i))
      .collect(Collectors.toList());

    assertThat(result, equalTo(List.of(1, 2, 3)));
  }
}
