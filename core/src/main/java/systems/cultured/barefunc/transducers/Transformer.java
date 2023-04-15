package systems.cultured.barefunc.transducers;

public abstract class Transformer<I, O> { 
  public abstract <A> Reducer<A, I> apply(Reducer<A, O> downstream);

  public final <P> Transformer<I, P> compose(Transformer<O, P> other) {
    return new Transformer<I, P>() {
      @Override
      public <A> Reducer<A, I> apply(Reducer<A, P> downstream) {
        return Transformer.this.apply(other.apply(downstream));
      }      
    };
  }
}
