package systems.cultured.barefunc.transducers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import systems.cultured.barefunc.transducers.Transducer.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;

class TransducerTest {
  @Test
  void mapTransducerMaps() {
    List<Integer> initialAcc = List.of(1, 2, 3);
    int initialItem = 4;
    AtomicInteger resultItem = new AtomicInteger();
    
    List<Integer> resultAcc = Transducer.<Integer, Integer, List<Integer>>map(x -> x + 1)
      .apply((acc, item) -> {
        resultItem.set(item);
        return acc;
      }).reduce(initialAcc, initialItem);

    assertThat(resultAcc, equalTo(initialAcc));
    assertThat(resultItem.get(), equalTo(initialItem + 1));
  }

  @Test
  void filterTransducerIncludesMatching() {
    List<Integer> initialAcc = List.of(1, 2, 3);
    int initialItem = 4;
    AtomicInteger resultItem = new AtomicInteger();
    
    List<Integer> resultAcc = Transducer.<Integer, List<Integer>>filter(x -> x == 4)
      .apply((acc, item) -> {
        resultItem.set(item);
        return acc;
      }).reduce(initialAcc, initialItem);

    assertThat(resultAcc, equalTo(initialAcc));
    assertThat(resultItem.get(), equalTo(initialItem));
  }

  @Test
  void filterTransducerDoesNotIncludeIfNotMatching() {
    List<Integer> initialAcc = List.of(1, 2, 3);
    int initialItem = 4;
    
    List<Integer> resultAcc = Transducer.<Integer, List<Integer>>filter(x -> x != 4)
      .apply((acc, item) -> {
        fail("Downstream called when item not matching");
        return acc;
      }).reduce(initialAcc, initialItem);

    assertThat(resultAcc, equalTo(initialAcc));
  }

  @Test
  void composeCallsBothTransducers() {
    List<String> initialAcc = List.of("a", "b");
    String initialItem = "initial";
    AtomicReference<String> resultItem = new AtomicReference<>();
    Map<String, String, Object> firstMap = Transducer.map(x -> x + " first");
    Map<String, String, Object> secondMap = Transducer.map(x -> x + " second");

    Transducer.compose(firstMap, secondMap).apply((acc, item) -> {
      resultItem.set(item);
      return acc;
    }).reduce(initialAcc, initialItem);

    assertThat(resultItem.get(), equalTo("initial first second"));
  }
}
