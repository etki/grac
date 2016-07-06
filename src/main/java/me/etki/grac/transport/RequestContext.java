package me.etki.grac.transport;

import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.transport.trace.Trace;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class RequestContext {

    private final RetryPolicy retryPolicy;
    private final Trace trace;

    public RequestContext(RetryPolicy retryPolicy, Trace trace) {
        this.retryPolicy = retryPolicy;
        this.trace = trace;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public Trace getTrace() {
        return trace;
    }
}
