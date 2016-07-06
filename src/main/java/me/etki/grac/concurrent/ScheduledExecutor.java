package me.etki.grac.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface ScheduledExecutor {

    default CompletableFuture<Void> schedule(Runnable command, long delay, TimeUnit unit) {
        Callable<Void> callable = () -> {
            command.run();
            return null;
        };
        return  schedule(callable, delay, unit);
    }
    <V> CompletableFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

    void shutdown();
    void shutdownNow();
}
