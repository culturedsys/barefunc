package systems.cultured.barefunc.transducers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Reducers {
  private Reducers() { }

  public static <T, U> Reducer<List<U>, T> map(Function<T, U> f) {
    return (acc, t) -> {
      var result = new ArrayList<>(acc);
      result.add(f.apply(t));
      
      return result;
    };
  }
  
  public static <T> Reducer<List<T>, T> filter(Predicate<T> p) {
    return (acc, t) -> {
      if (p.test(t)) {
        var result = new ArrayList<>(acc);
        result.add(t);

        return result;
      }

      return acc;
    };
  }

}
