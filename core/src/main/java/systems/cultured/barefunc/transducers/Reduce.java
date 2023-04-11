package systems.cultured.barefunc.transducers;

import java.util.Collection;
import java.util.function.Supplier;


public class Reduce<T, U> {
  private Collection<T> source;
  private Transformer<T, U> transformer;

  public Reduce(Collection<T> source, Transformer<T, U> transformer) {
    this.source = source;
    this.transformer = transformer;
  }

  public static <T> Reduce<T, T> over(Collection<T> source) {
    return new Reduce<>(source, Transformers.identity());
  }

  public <V> Reduce<T, V> compose(Transformer<U, V> transformer) {
    return new Reduce<>(source, this.transformer.compose(transformer));
  }

  public <V> Reduce<T, V> compose(Supplier<Transformer<U, V>> transformer) {
    return new Reduce<>(source, this.transformer.compose(transformer.get()));
  }

  public <A> A reduce(Reducer<A, U> finaliser, A init) {
    var acc = init;
    var reducer = transformer.apply(finaliser);
    for(T t : source) {
      var result = reducer.apply(acc, t);
      if (result instanceof Reducer.Continue<A> c) {
        acc = c.value();
      } else {
        break;
      }
    }

    return reducer.finish(acc);
  }
}
