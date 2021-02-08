# Barely Functional - Functional utilities, Java idioms

Barely Functional attempts to provide functional programming utilities without
straying too far from common Java idioms.

## Result

Represents the result of an operation that may fail - either the result value of
the operation, for an `Ok` result, or information about the error, if an `Error`
result.

```java
Result<Integer, String> safeDivide(int dividend, int divisor) {
    if (divisor == 0) {
        return Result.error("Divide by 0");
    } else {
        return Result.ok(dividend / divisor);
    }
}
```

Provides a general method for processing either an `Ok` or `Error` result:

```java
String message = result.handle(
    division -> "The result of the division is " + division, 
    error -> "Could not perform division: " + error);
```

as well as various utility functions:

```java
Result<Double, String> mapped = result.map(value -> value / 2.0);

int ratio = result.orElse(0);

int ratio = result.orElseThrow(error -> new IllegalArgumentException(error));
```