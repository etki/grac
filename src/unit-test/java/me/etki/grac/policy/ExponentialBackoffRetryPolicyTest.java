package me.etki.grac.policy;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
@RunWith(JUnitParamsRunner.class)
public class ExponentialBackoffRetryPolicyTest {

    public Object[] dataProvider() {
        long minimum = 100;
        long maximum = 10000;
        Function<Integer, Long> expectedGenerator
                = attempt -> Math.min(maximum, (long) (Math.pow(2, attempt) * minimum));
        return Stream.iterate(2, i -> i + 1)
                .map(attempt -> new Object[] { attempt, minimum, maximum, expectedGenerator.apply(attempt) })
                .limit(10)
                .toArray();
    }

    @Test
    @Parameters(method = "dataProvider")
    public void shouldYieldCorrectValues(int attempt, long minimum, long maximum, long expected) {
        TimeRange range = new TimeRange(minimum, maximum, 0.0);
        assertEquals(expected, new ExponentialBackoffRetryPolicy(range).calculateDelay(attempt));
        double randomFactor = 0.5;
        range = new TimeRange(minimum, maximum, randomFactor);
        long value = new ExponentialBackoffRetryPolicy(range).calculateDelay(attempt);
        assertTrue(value <= (long) (expected * (1.0 + randomFactor)));
        assertTrue(value >= (long) (expected * (1.0 - randomFactor)));
    }

    @Test
    public void shouldApplyRandomFactor() {

        TimeRange range = new TimeRange(125, 2000, 0.2);
        ExponentialBackoffRetryPolicy policy = new ExponentialBackoffRetryPolicy(range);
        List<Long> exactValues = Stream.generate(() -> 1000L).limit(10).collect(Collectors.toList());
        List<Long> generatedValues = Stream.generate(() -> null)
                .map(v -> policy.calculateDelay(4))
                .limit(10)
                .collect(Collectors.toList());
        assertNotEquals(exactValues, generatedValues);
    }
}