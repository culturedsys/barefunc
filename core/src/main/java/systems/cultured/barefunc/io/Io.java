package systems.cultured.barefunc.io;

import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface Io<T> {
  static <U> Io<U> pure(U value) {
    return new Pure<>(value);
  }

  static <U> Io<U> suspend(Supplier<U> supplier) {
    return new Suspend<>(supplier);
  }

  default <U> Io<U> flatMap(Function<T, Io<U>> f) {
    return new FlatMap<>(this, f);
  }

  default T run() {
    return match(pure -> pure.value(), 
        suspend -> suspend.supplier().get(), 
        flatMap -> flattenFlatMap(flatMap).run()
    );
  }

  private static <S, T> Io<T> flattenFlatMap(FlatMap<S, T> flatMap) {
    return flatMap.f().apply(flatMap.previous().run());
  }

  <U> U match(Function<Pure<T>, U> pure,
      Function<Suspend<T>, U> suspend,
      Function<FlatMap<?, T>, U> flatMap
  );
}

record Pure<T>(T value) implements Io<T> {
  @Override
  public <U> U match(Function<Pure<T>, U> pure, 
      Function<Suspend<T>, U> suspend, 
      Function<FlatMap<?, T>, U> flatMap) {
    return pure.apply(this);
  }
}

record Suspend<T>(Supplier<T> supplier) implements Io<T> {
  @Override
  public <U> U match(Function<Pure<T>, U> pure, 
      Function<Suspend<T>, U> suspend, 
      Function<FlatMap<?, T>, U> flatMap) {
    return suspend.apply(this);
  }
}

record FlatMap<S, T>(Io<S> previous, Function<S, Io<T>> f) implements Io<T> {
  @Override
  public <U> U match(Function<Pure<T>, U> pure,
      Function<Suspend<T>, U> suspend,
      Function<FlatMap<?, T>, U> flatMap) {
    return flatMap.apply(this);
  }
}