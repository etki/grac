package me.etki.grac.policy;

import me.etki.grac.utility.MathUtilities;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ExponentialBackoffRetryPolicy extends AbstractRetryPolicy {

    public static final long DEFAULT_MINIMUM_DELAY = 100;
    public static final long DEFAULT_MAXIMUM_DELAY = 10000;

    private final TimeRange range;

    public ExponentialBackoffRetryPolicy(
            TimeRange range,
            int maximumAttempts,
            boolean shouldRetryOnServerError,
            boolean shouldRetryOnClientError) {

        super(maximumAttempts, shouldRetryOnServerError, shouldRetryOnClientError);
        this.range = range;
    }

    public ExponentialBackoffRetryPolicy(
            TimeRange range,
            int maximumAttempts,
            boolean shouldRetryOnServerError) {

        super(maximumAttempts, shouldRetryOnServerError);
        this.range = range;
    }

    public ExponentialBackoffRetryPolicy(TimeRange range, int maximumAttempts) {
        super(maximumAttempts);
        this.range = range;
    }

    public ExponentialBackoffRetryPolicy(TimeRange range) {
        this.range = range;
    }

    public ExponentialBackoffRetryPolicy() {
        this(new TimeRange(DEFAULT_MINIMUM_DELAY, DEFAULT_MAXIMUM_DELAY));
    }

    @Override
    public long calculateDelay(int attempt) {
        int i = 0;
        int multiplier = 1;
        while (i++ < attempt) {
            multiplier *= 2;
        }
        long value = Math.min(range.getMinimumDelay() * multiplier, range.getMaximumDelay());
        return MathUtilities.applyRandomFactor(value, range.getRandomFactor());
    }
}
