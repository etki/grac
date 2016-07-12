package me.etki.grac.policy;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * todo: make tests for hard cases (e.g. when latency is small and can't be divided good enough)
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
@RunWith(JUnitParamsRunner.class)
public class LinearBackoffRetryPolicyTest {

    public Object[] dataProvider() {
        return Stream.iterate(2, i -> i + 1)
                .limit(10)
                .map(i -> new Object[] { i, 6, 100, 500, Math.min(500, (i - 1) * 100) })
                .toArray();
    }

    @Test
    @Parameters(method = "dataProvider")
    public void shouldGenerateCorrectValues(int attempt, int maximumAttempts, long minimum, long maximum, long expected) {
        TimeRange range = new TimeRange(minimum, maximum, 0.0);
        assertEquals(expected, new LinearBackoffRetryPolicy(range, maximumAttempts).calculateDelay(attempt));
        double randomFactor = 0.2;
        range = new TimeRange(minimum, maximum, randomFactor);
        long value = new LinearBackoffRetryPolicy(range, maximumAttempts).calculateDelay(attempt);
        assertTrue(value >= expected * (1.0 - randomFactor));
        assertTrue(value <= expected * (1.0 + randomFactor));
    }

    @Test
    public void shouldApplyRandomFactor() {

        TimeRange range = new TimeRange(0, 2000, 0.2);
        LinearBackoffRetryPolicy policy = new LinearBackoffRetryPolicy(range, 11);
        List<Long> exactValues = Stream.generate(() -> 1000L).limit(10).collect(Collectors.toList());
        List<Long> generatedValues = Stream.generate(() -> null)
                .map(v -> policy.calculateDelay(6))
                .limit(10)
                .collect(Collectors.toList());
        assertNotEquals(exactValues, generatedValues);
    }
}