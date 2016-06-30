package me.etki.grac.policy;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface RetryPolicy {

    boolean shouldRetryOnClientError();
    boolean shouldRetryOnServerError();
    boolean shouldPerformAttempt(int attempt);
    long calculateDelay(int attempt);
    int getMaximumAttempts();
}
