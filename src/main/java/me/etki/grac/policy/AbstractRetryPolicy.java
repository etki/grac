package me.etki.grac.policy;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public abstract class AbstractRetryPolicy implements RetryPolicy {

    public static final int DEFAULT_MAXIMUM_ATTEMPTS_VALUE = 1;
    public static final boolean DEFAULT_SHOULD_RETRY_ON_CLIENT_ERROR_VALUE = false;
    public static final boolean DEFAULT_SHOULD_RETRY_ON_SERVER_ERROR_VALUE = false;

    private final int maximumAttempts;
    private final boolean shouldRetryOnServerError;
    private final boolean shouldRetryOnClientError;

    public AbstractRetryPolicy(
            int maximumAttempts,
            boolean shouldRetryOnServerError,
            boolean shouldRetryOnClientError) {

        this.maximumAttempts = maximumAttempts;
        this.shouldRetryOnServerError = shouldRetryOnServerError;
        this.shouldRetryOnClientError = shouldRetryOnClientError;
    }

    public AbstractRetryPolicy(int maximumAttempts, boolean shouldRetryOnServerError) {
        this(maximumAttempts, shouldRetryOnServerError, DEFAULT_SHOULD_RETRY_ON_CLIENT_ERROR_VALUE);
    }

    public AbstractRetryPolicy(int maximumAttempts) {
        this(maximumAttempts, DEFAULT_SHOULD_RETRY_ON_SERVER_ERROR_VALUE);
    }

    public AbstractRetryPolicy() {
        this(DEFAULT_MAXIMUM_ATTEMPTS_VALUE);
    }

    @Override
    public boolean shouldRetryOnClientError() {
        return shouldRetryOnClientError;
    }

    @Override
    public boolean shouldRetryOnServerError() {
        return shouldRetryOnServerError;
    }

    @Override
    public boolean shouldPerformAttempt(int attempt) {
        return attempt <= maximumAttempts;
    }

    abstract public long calculateDelay(int attempt);

    @Override
    public int getMaximumAttempts() {
        return maximumAttempts;
    }
}
