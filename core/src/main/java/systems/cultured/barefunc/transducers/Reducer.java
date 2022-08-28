package systems.cultured.barefunc.transducers;

public interface Reducer<A, O> {
  O reduce(O m, A a);
}