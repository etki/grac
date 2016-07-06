package me.etki.grac.policy;

import me.etki.grac.transport.ServerResponse;
import me.etki.grac.transport.ResponseStatus;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface RetryPolicy {

    boolean shouldRetryOnClientError();
    boolean shouldRetryOnServerError();
    long calculateDelay(int attempt);
    int getMaximumAttempts();

    default boolean shouldPerformAttempt(int attempt) {
        return attempt <= getMaximumAttempts();
    }

    @SuppressWarnings("RedundantIfStatement")
    default boolean shouldRetry(ServerResponse response, Throwable throwable, int attempt) {
        if (!shouldPerformAttempt(attempt + 1)) {
            return false;
        }
        if (throwable != null) {
            return true;
        }
        if (ResponseStatus.OK.equals(response.getStatus())) {
            return false;
        }
        if (ResponseStatus.CLIENT_ERROR.equals(response.getStatus()) && !shouldRetryOnClientError()) {
            return false;
        }
        if (ResponseStatus.SERVER_ERROR.equals(response.getStatus()) && !shouldRetryOnServerError()) {
            return false;
        }
        return true;
    }
}
