package systems.cultured.barefunc.flatteners;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Collector.Characteristics;

import systems.cultured.barefunc.result.Result;

/**
 * Provides collectors for "flattening" streams, that is, converting streams of some type containing a value, to instances of
 * that type, containing the result of collecting the stream of those values.
 */
public class Flatteners {
    private Flatteners() { }

    /**
     * Produces a Collector that collects a stream of Result<T, E> into an Ok result of a List<T> containing all values, if all
     * results in the stream are Ok, or an Error result containing one of the errors in the stream, if at least one result in the
     * stream is an error.
     */
    public static <T, E> Collector<Result<T, E>, ?, Result<List<T>, E>> result() {
        return result(Collectors.<T>toList(), Collectors.collectingAndThen(Collectors.<E>reducing((a, b) -> a), Optional::get));
    }

    /**
     * Produces a collector which, if the stream contains only Ok result, uses the okCollector to collect these values into an Ok
     * result, or, if the stream contains any Error results, uses the errorCollector to collect these errors into an Error result.
     */
    public static <T, E, A, EA, C, EC> Collector<Result<T, E>, ?, Result<C, EC>> result(Collector<T, A, C> okCollector,
            Collector<E, EA, EC> errorCollector) {
        return Collector.of(
            () -> ResultAccumulator.supply(okCollector, errorCollector),
            ResultAccumulator::accumulate,
            ResultAccumulator::combine,
            ResultAccumulator::finish,
            deriveCharacteristics(okCollector, errorCollector).toArray(new Characteristics[0])
        );
    }

    private static <T, E, A, EA, C, EC> Set<Characteristics> 
            deriveCharacteristics(Collector<T, A, C> collector, Collector<E, EA, EC> errorCollector) {
        var characteristics = collector.characteristics();
        var errorCharacteristics = errorCollector.characteristics();
        var combinedCharacteristics = new HashSet<Collector.Characteristics>();

        if (characteristics.contains(Collector.Characteristics.CONCURRENT) && 
                errorCharacteristics.contains(Collector.Characteristics.CONCURRENT)) {
            combinedCharacteristics.add(Collector.Characteristics.CONCURRENT);
        }

        if (characteristics.contains(Collector.Characteristics.UNORDERED) || 
               errorCharacteristics.contains(Collector.Characteristics.UNORDERED)) {
            combinedCharacteristics.add(Collector.Characteristics.UNORDERED);
        }

        return combinedCharacteristics;
    }
}
