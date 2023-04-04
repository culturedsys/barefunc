package systems.cultured.barefunc.transducers;

import java.util.Collection;


public class Reduce<T> {
  private Collection<T> source;

  public Reduce(Collection<T> source) {
    this.source = source;
  }

  public static <T> Reduce<T> over(Collection<T> source) {
    return new Reduce<>(source);
  }

  public <A> A reduce(Reducer<A, T> reducer, A init) {
    var acc = init;
    for(T t : source) {
      acc = reducer.apply(acc, t);
    }

    return acc;
  }

}
