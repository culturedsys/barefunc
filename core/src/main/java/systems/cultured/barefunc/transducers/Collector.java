package systems.cultured.barefunc.transducers;

import java.util.function.Consumer;
import java.util.function.Supplier;

import systems.cultured.barefunc.Unit;

public interface Collector<R, T> extends Supplier<R>, Consumer<T>, Reducer<Unit, T> {
  default Result<Unit> apply(Unit acc, T t) {
    accept(t);
    return new Continue<>(acc);
  }
}
