package systems.cultured.barefunc.transducers;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReducerTest {
  @Test
  void canSum() {
    var result = Reduce.over(List.of(1, 2, 3))
      .reduce((a, t) -> a + t, 0);

    assertThat(result, equalTo(6));
  }  

  @Test
  void canMap() {
    var result = Reduce.over(List.of("hello", "world"))
      .reduce(Transformers.map((String s) -> s.toUpperCase()).apply(Transformers::addToList), 
        Collections.emptyList());

    assertThat(result, equalTo(List.of("HELLO", "WORLD")));
  }

  @Test
  void canFilter() {
    var result = Reduce.over(List.of(1, 2, 3, 4, 5))
      .reduce(Transformers.filter((Integer x) -> x % 2 == 0).apply(Transformers::addToList), 
        Collections.emptyList());

    assertThat(result, equalTo(List.of(2, 4)));
  }

  @Test
  void canCompose() {
    var result = Reduce.over(List.of(1, 2, 3, 4, 5))
      .compose(Transformers.filter((Integer x) -> x % 2 == 0))
      .compose(Transformers.map((Integer x) -> x * 2))
      .reduce(Transformers::addToList, Collections.emptyList());

    assertThat(result, equalTo(List.of(4, 8)));
  }
}
