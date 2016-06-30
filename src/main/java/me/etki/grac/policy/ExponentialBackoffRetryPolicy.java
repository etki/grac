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

    private final RetryPolicyTimeRange range;

    public ExponentialBackoffRetryPolicy(
            RetryPolicyTimeRange range,
            int maximumAttempts,
            boolean shouldRetryOnServerError,
            boolean shouldRetryOnClientError) {

        super(maximumAttempts, shouldRetryOnServerError, shouldRetryOnClientError);
        this.range = range;
    }

    public ExponentialBackoffRetryPolicy(
            RetryPolicyTimeRange range,
            int maximumAttempts,
            boolean shouldRetryOnServerError) {

        super(maximumAttempts, shouldRetryOnServerError);
        this.range = range;
    }

    public ExponentialBackoffRetryPolicy(RetryPolicyTimeRange range, int maximumAttempts) {
        super(maximumAttempts);
        this.range = range;
    }

    public ExponentialBackoffRetryPolicy(RetryPolicyTimeRange range) {
        this.range = range;
    }

    public ExponentialBackoffRetryPolicy() {
        this(new RetryPolicyTimeRange(DEFAULT_MINIMUM_DELAY, DEFAULT_MAXIMUM_DELAY));
    }

    @Override
    public long calculateDelay(int attempt) {
        int i = 0;
        long buffer = 1;
        while (i++ < attempt) {
            buffer *= MathUtilities.applyRandomFactor(range.getMinimumDelay(), range.getRandomFactor());
        }
        long maximum = MathUtilities.applyRandomFactor(range.getMaximumDelay(), range.getRandomFactor());
        long minimum = MathUtilities.applyRandomFactor(range.getMinimumDelay(), range.getRandomFactor());
        return MathUtilities.limit(buffer, minimum, maximum);
    }
}
