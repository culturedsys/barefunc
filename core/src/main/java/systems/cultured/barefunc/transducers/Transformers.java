package systems.cultured.barefunc.transducers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Transformers {
  public abstract static class Forwarding<A, O, T> implements Reducer<A, T> {
    protected final Reducer<A, O> downstream;
  
    protected Forwarding(Reducer<A, O> downstream) {
      this.downstream = downstream;
    }
  
    @Override
    public A finish(A acc) {
      return downstream.finish(acc);
    }
  }

  private Transformers() { }

  public static <T> Reducer.Result<List<T>> addToList(List<T> list, T t) {
    var result = new ArrayList<>(list);
    result.add(t);
    
    return new Reducer.Continue<>(result);
  }

  public static <T, U> Transformer<T, U> map(Function<T, U> f) {
    return new Transformer<T, U>() {
      @Override
      public <A> Reducer<A, T> apply(Reducer<A, U> downstream) {
        return new Transformers.Forwarding<>(downstream) {
          public Reducer.Result<A> apply(A acc, T t) {
            return downstream.apply(acc, f.apply(t));
          }
        };
      }
    };
  }

  public static <T> Transformer<T, T> filter(Predicate<T> p) {
    return new Transformer<T, T>() {
      @Override
      public <A> Reducer<A, T> apply(Reducer<A, T> downstream) {
        return new Transformers.Forwarding<>(downstream) {
          public Reducer.Result<A> apply (A acc, T t) {
            if (p.test(t)) {
              return downstream.apply(acc, t);
            }

            return new Reducer.Continue<>(acc);
          }      
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

  public static <T> Transformer<T, T> takeWhile(Predicate<T> p) {
    return new Transformer<T,T>() {
      @Override
      public <A> Reducer<A, T> apply(Reducer<A, T> downstream) {
        return new Transformers.Forwarding<>(downstream) {
          public Reducer.Result<A> apply(A acc, T t) {
            if (!p.test(t)) {
              return new Reducer.Stop<>();
            }  
            return downstream.apply(acc, t);
          }
        };
      }      
    };
  }

  public static <T> Transformer<T, T> sorted(Comparator<? super T> comparator) {
    return new Transformer<T, T>() {
      @Override
      public <A> Reducer<A, T> apply(Reducer<A, T> downstream) {
        return new Reducer<A, T>() {
          private final PriorityQueue<T> buffer = new PriorityQueue<>(comparator);

          public Reducer.Result<A> apply(A acc, T t) {
            buffer.add(t);
            return new Reducer.Continue<>(acc);
          }

          @Override
          public A finish(A acc) {
            while(!buffer.isEmpty()) {
              var r = downstream.apply(acc, buffer.remove());
              if (r instanceof Reducer.Continue<A> c) {
                acc = c.value();
              } else {
                break;
              }
            }

            return downstream.finish(acc);
          }
        };
      }
    };
  }

  public static <T extends Comparable<? super T>> Transformer<T, T> sorted() {
    return sorted(Comparator.naturalOrder());
  }

  public static <T> Supplier<Collector<List<T>, T>> toList() {
    return () -> new Collector<>() {
      private final List<T> result = new ArrayList<>();

      @Override
      public List<T> get() {
        return result;
      }

      @Override
      public void accept(T t) {
        result.add(t);
      }      
    };
  }
}
