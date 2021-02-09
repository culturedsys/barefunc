package systems.cultured.barefunc.flatteners;

import java.util.stream.Collector;

import systems.cultured.barefunc.result.Result;

class ResultAccumulator<T, E, A, EA, C, EC> {
    A acc;
    EA errorAcc;
    boolean isOk;
    Collector<T, A, C> collector;
    Collector<E, EA, EC> errorCollector;

    public ResultAccumulator(Collector<T, A, C> collector, Collector<E, EA, EC> errorCollector) {
        this.acc = collector.supplier().get();
        this.errorAcc = errorCollector.supplier().get();
        this.isOk = true;
        this.collector = collector;
        this.errorCollector = errorCollector;
    }
    
    public void accumulate(Result<T, E> result) {
        if (isOk) {
            result.handleAction(
                    value -> collector.accumulator().accept(acc, value), 
                    error -> {
                        errorCollector.accumulator().accept(errorAcc, error);
                        isOk = false;
                    });
        } else {
            result.handleAction(value -> {}, error -> errorCollector.accumulator().accept(errorAcc, error));
        }    
    }

    public ResultAccumulator<T, E, A, EA, C, EC> combine(ResultAccumulator<T, E, A, EA, C, EC> right) {
        if (isOk && right.isOk) {
            acc = collector.combiner().apply(acc, right.acc);
            return this;
        } else if (isOk) {
            return right;
        } else if (right.isOk) {
            return this;
        } else {
            errorAcc = errorCollector.combiner().apply(errorAcc, right.errorAcc);
            return this;
        } 
    }

    public Result<C, EC> finish() {
        if (isOk) {
            return Result.ok(collector.finisher().apply(acc));
        } else {
            return Result.error(errorCollector.finisher().apply(errorAcc));
        }
    }

	public static <T, E, A, EA, C, EC> ResultAccumulator<T, E, A, EA, C, EC> 
            supply(Collector<T, A, C> okCollector, Collector<E, EA, EC> errorCollector) {
		return new ResultAccumulator<>(okCollector, errorCollector);
	}
}
