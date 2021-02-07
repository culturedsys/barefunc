package systems.cultured.barefunc.result;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents the result of an operation that might fail. A result can either be Ok, in which case it contains a value of T, or
 * Error, in which case it has a value of E. Result also provides methods for safely handling these two cases. 
 */
public abstract class Result<T, E> {
    Result() { }

    /**
     * Processes this result using one function if this result is Ok, and another if this result is Error.
     * 
     * @param <U> the type which both handler functions produce
     * @param okHandler the handler function called if this is Ok
     * @param errorHander the handler function called if this is Error
     * @return the return value of whichever handler function was called
     */
    public abstract <U> U handle(Function<? super T, ? extends U> okHandler, Function<? super E, ? extends U> errorHandler);

    public void handleAction(Consumer<? super T> okConsumer, Consumer<? super E> errorConsumer) {
        handle(value -> {
            okConsumer.accept(value);
            return null;
        }, error -> {
            errorConsumer.accept(error);
            return null;
        });
    }

    /**
     * @param defaultValue the value to use if this is Error
     * @return the value contained in this if it is Ok, or defaultValue
     */
    public T orElse(T defaultValue) {
        return handle(Function.identity(), error -> defaultValue);
    }    

    /**
     * Processes the value of an Ok result.
     * 
     * @param <U> the type that the value is transformed to
     * @param f function called if this is an Ok result
     * @return an Ok result containing the return value of f, if this is Ok, or an Error result containing the same error as this,
     *         if this is an error
     */
    public <U> Result<U, E> map(Function<? super T, U> f) {
        return handle(value -> Result.ok(f.apply(value)), Result::error);
    }

    @Override
    public String toString() {
        return handle(value -> String.format("Ok(%s)", value), error -> String.format("Error(%s)", error));
    }

    /**
     * Creates an Ok result containing the provided value. 
     * 
     * @param value
     */
    public static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    /**
     * Creates an Error result containing the provided error information
     */
    public static <T, E> Result<T, E> error(E error) {
        return new Error<>(error);
    }

    public static final class Ok<T, E> extends Result<T, E> {
        private final T value;

        private Ok(T value) {
            this.value = value;
        }

        @Override
        public <U> U handle(Function<? super T, ? extends U> okHandler, Function<? super E, ? extends U> errorHandler) {
            return okHandler.apply(value);
        }
    }

    public static final class Error<T, E> extends Result<T, E> {
        private final E error;

        private Error(E error) {
            this.error = error;
        }

        @Override
        public <U> U handle(Function<? super T, ? extends U> okHandler, Function<? super E, ? extends U> errorHandler) {
            return errorHandler.apply(error);
        }
    }
}
