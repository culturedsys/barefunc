package systems.cultured.barefunc.transducers;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Transducer<A, B, O> {
  Reducer<B, O> apply(Reducer<A, O> downstream);

  static <A, B, C, O> Transducer<C, B, O> compose(Transducer<A, B, O> outer, Transducer<C, A, O> other) {
    return downstream -> outer.apply(other.apply(downstream));
  }

  static <A, B, O> Map<A, B, O> map(Function<B, A> f) {
    return new Map<>(f);
  }

  static <A, O> Filter<A, O> filter(Predicate<A> p) {
    return new Filter<>(p);
  }

  class Map<A, B, O> implements Transducer<A, B, O> {
    private final Function<B, A> f;

    public Map(Function<B, A> f) {
      this.f = f;
    }

    @Override
    public Reducer<B, O> apply(Reducer<A, O> downstream) {
      return (acc, item) -> downstream.reduce(acc, f.apply(item));
    }
  }

  class Filter<A, O> implements Transducer<A, A, O> {
    private final Predicate<A> p;
    public Filter(Predicate<A> p) {
      this.p = p;
    }

    @Override
    public Reducer<A, O> apply(Reducer<A, O> downstream) {
      return (acc, item) -> {
        if (p.test(item)) {
          return downstream.reduce(acc, item);
        } else {
          return acc;
        }
      };
    }
  }
}
