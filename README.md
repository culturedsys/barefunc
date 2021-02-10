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

## Flatteners

Provides Collectors which convert a Stream of some type that contains a value,
to an instance of that type containing a collected result.

### Result

For a Stream of `Result<T, E>`, the result depends on whether the stream
contains any Error results. If the Stream only contains Ok results, the Result
flattener produces an Ok result, containing the result of collecting all the
values within these Ok results. If the Stream contains any Error results, the
flattener produces an Error result containing the result of collecting the
Errors in the Stream.

The Collectors used for collecting values and errors can be specified. If they
are not specified, Ok results are collected to a list of all values, and Error
results are collected to one of these Errors (the first in encounter order).

Using the default collectors:

```java
// Produces Ok([1, 2, 3])
Stream.of(Result.ok(1), Result.ok(2), Result.ok(3))
        .collect(Flatteners.result());

// Produces Error(2)
Stream.of(Result.ok(1), Result.error(2), Result.error(3))
        .collect(Flatteners.result());
```

Or, if the collectors are specified:

```java
//Produces "1,2,3"
Stream.of(Result.ok(1), Result.ok(2), Result.ok(3))
        .collect(Flatteners.result(Collectors.joining(","), 
                Collectors.toSet()));

// Produces Set(2, 3)
Stream.of(Result.ok(1), Result.error(2), Result.error(3))
        .collect(Flatteners.result(Collectors.joining(","), 
                Collectors.toSet()));
```

## Copyright
Copyright 2021 Tim Fisken 

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.
