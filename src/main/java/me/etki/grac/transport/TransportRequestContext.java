package me.etki.grac.transport;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportRequestContext {

    private final AtomicInteger attempt = new AtomicInteger(0);

    public int getAttempt() {
        return attempt.get();
    }

    public int incrementAttempt() {
        return attempt.incrementAndGet();
    }


}
