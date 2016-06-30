package me.etki.grac.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ScheduledHelper {

    private final ScheduledExecutor executor;

    public ScheduledHelper(ScheduledExecutor executor) {
        this.executor = executor;
    }

    public CompletableFuture<Void> await(long delay, TimeUnit unit) {
        return executor.schedule(() -> null, delay, unit);
    }

    public <T> CompletableFuture<Void> setTimeout(CompletableFuture<T> future, long delay, TimeUnit unit) {
        return executor.schedule(() -> {
            String message = "CompletableFuture has timed out after delay of " + delay + " " + unit;
            future.completeExceptionally(new TimeoutException(message));
        }, delay, unit);
    }
}
