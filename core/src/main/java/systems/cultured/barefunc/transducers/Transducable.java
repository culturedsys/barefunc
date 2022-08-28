package systems.cultured.barefunc.transducers;

import java.util.stream.Stream;

public interface Transducable<A, B, O> {
  O transduce(Transducer<A, B, O> transducer);

  static <A, B> Stream<A> transduceStream(Stream<B> input, 
    Transducer<A, B, Stream<A>> transducer) {
    return new TransducableStream<A, B>(input)
      .transduce(transducer);
  }

  class TransducableStream<A, B> implements Transducable<A, B, Stream<A>> {
    private final Stream<B> input;

    public TransducableStream(Stream<B> input) {
      this.input = input;
    }

    @Override
    public Stream<A> transduce(Transducer<A, B, Stream<A>> transducer) {
      return input.<Stream<A>>reduce(Stream.empty(), 
          (acc, item) -> appender(transducer).reduce(acc, item), 
          Stream::concat);
    }

    private Reducer<B, Stream<A>> appender(Transducer<A, B, Stream<A>> transducer) {
      return transducer.apply((a, i) -> Stream.concat(a, Stream.of(i)));
    }
  }
}
