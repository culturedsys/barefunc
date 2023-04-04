package systems.cultured.barefunc.transducers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Transformers {
  private Transformers() { }

  public static <T> List<T> addToList(List<T> list, T t) {
    var result = new ArrayList<>(list);
    result.add(t);
    
    return result;
  }

  public static <T, U> Transformer<T, U> map(Function<T, U> f) {
    return new Transformer<T, U>() {
      @Override
      public <A> Reducer<A, T> apply(Reducer<A, U> downstream) {
        return (acc, t) -> downstream.apply(acc, f.apply(t));
      }
    };
  }

  public static <T> Transformer<T, T> filter(Predicate<T> p) {
    return new Transformer<T, T>() {
      @Override
      public <A> Reducer<A, T> apply(Reducer<A, T> downstream) {
        return (acc, t) -> {
          if (p.test(t)) {
            return downstream.apply(acc, t);
          }
    
          return acc;
        };
      }      
    };
  }

  public static <T> Transformer<T, T> identity() {
    return new Transformer<T,T>() {
      @Override
      public <A> Reducer<A, T> apply(Reducer<A, T> downstream) {
        return downstream;
      }
    };
  }
}
