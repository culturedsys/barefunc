package systems.cultured.barefunc.transducers;

import java.util.function.BiFunction;

public interface Reducer<A, T> extends BiFunction<A, T, Reducer.Result<A>> {   
  default A finish(A acc) {
    return acc;
  }
  
  sealed interface Result<T> { }
  record Continue<T>(T value) implements Result<T> { }
  record Stop<T>() implements Result<T> { }
}
