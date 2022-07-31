package systems.cultured.barefunc.tailcall;

import java.util.function.Supplier;

/**
 * Reifies tail calls, so that they can be evaluated without increasing the stack for each call.
 * 
 * Consider the tail-recursive implementation of factorial using an accumulator:
 * 
 * <pre>{@code
 * 
 * BigDecimal factorial(int n) {
 *  return factorial(n, BigDecimal.ONE);
 * }
 * 
 * BigDecimal factorial(int n, BigDecimal acc) {
 *  if (n <= 1) {
 *    return acc;
 *  }
 * 
 *  return factorial(n - 1, acc.multiply(BigDecimal.valueOf(n)));
 * }
 * }</pre>
 * 
 * This will create a new stack frame for every invocation, so for {@code n} larger than about 7000, it will
 * overflow the stack on a stock JVM. To prevent this, you can reify the tail call. Instead of directly calling the
 * function recursively, you can return an object which will call the function when it is evaluated. As this evaluation
 * of a tail of calls can be done iteratively, it will use a constant amount of stack space no matter what {@code n}
 * is.
 * 
 * <pre>{@code
 * 
 * BigDecimal factorial(int n) {
 *  return factorial(n, BigDecimal.ONE).eval();
 * }
 * 
 * TailCall<BigDecimal> factorial(int n, BigDecimal acc) {
 *  if (n <= 1) {
 *    return TailCall.done(acc);
 *  }
 * 
 *  return TailCall.call(() -> factorial(n - 1, acc.multiply(BigDecimal.valueOf(n))));
 * }
 * }</pre>
 * 
 */
public sealed interface TailCall<T> {
  static <T> TailCall<T> done(T value) {
    return new Done<>(value);
  }

  static <T> TailCall<T> call(Supplier<TailCall<T>> supplier) {
    return new Call<>(supplier);
  }

  default T eval() {
    var current = this;
    while (current instanceof Call<T> call) {
      current = call.supplier().get();
    }

    return ((Done<T>) current).value();
  }
}

record Done<T>(T value) implements TailCall<T> {  }
record Call<T>(Supplier<TailCall<T>> supplier) implements TailCall<T> { }
