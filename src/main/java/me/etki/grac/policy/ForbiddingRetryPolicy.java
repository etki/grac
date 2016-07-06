package me.etki.grac.policy;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ForbiddingRetryPolicy implements RetryPolicy {

    public static final ForbiddingRetryPolicy INSTANCE = new ForbiddingRetryPolicy();

    @Override
    public boolean shouldRetryOnClientError() {
        return false;
    }

    @Override
    public boolean shouldRetryOnServerError() {
        return false;
    }

    @Override
    public long calculateDelay(int attempt) {
        return 0;
    }

    @Override
    public int getMaximumAttempts() {
        return 1;
    }

    @Override
    public int hashCode() {
        return ForbiddingRetryPolicy.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ForbiddingRetryPolicy;
    }

    @Override
    public String toString() {
        return "ForbiddingRetryPolicy";
    }
}
