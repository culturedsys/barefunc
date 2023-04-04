package systems.cultured.barefunc.transducers;

import java.util.function.BiFunction;

public interface Reducer<A, T> extends BiFunction<A, T, A> { }
