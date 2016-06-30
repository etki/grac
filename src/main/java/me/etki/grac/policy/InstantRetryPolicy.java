package me.etki.grac.policy;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class InstantRetryPolicy extends AbstractRetryPolicy {

    public InstantRetryPolicy(int maximumAttempts, boolean shouldRetryOnServerError, boolean shouldRetryOnClientError) {
        super(maximumAttempts, shouldRetryOnServerError, shouldRetryOnClientError);
    }

    public InstantRetryPolicy(int maximumAttempts, boolean shouldRetryOnServerError) {
        super(maximumAttempts, shouldRetryOnServerError);
    }

    public InstantRetryPolicy(int maximumAttempts) {
        super(maximumAttempts);
    }

    public InstantRetryPolicy() {
    }

    @Override
    public long calculateDelay(int attempt) {
        return 0;
    }
}
