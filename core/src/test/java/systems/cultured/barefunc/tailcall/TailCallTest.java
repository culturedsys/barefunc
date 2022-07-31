package systems.cultured.barefunc.tailcall;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class TailCallTest {
    private TailCall<BigInteger> factorial(int n, BigInteger acc) {
        if (n <= 1) {
            return TailCall.done(acc);
        } else {
            return TailCall.call(() -> factorial(n - 1, acc.multiply(BigInteger.valueOf(n))));
        }
    }

    @Test
    public void trampolineGivesCorrectAnswer() {
        TailCall<BigInteger> factorial = factorial(6, BigInteger.ONE);

        assertThat(factorial.eval(), equalTo(BigInteger.valueOf(720)));
    }
    @Test
    public void trampolineIsStackSafe() {
        TailCall<BigInteger> factorial = factorial(100000, BigInteger.ONE);

        factorial.eval();
         
        // Succeeds if no exception thrown
    }
}
