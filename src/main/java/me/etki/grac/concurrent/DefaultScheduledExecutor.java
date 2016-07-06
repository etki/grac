package me.etki.grac.concurrent;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Todo: does current code guarantee that none of futures will escape cancellation during shutdown call?
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultScheduledExecutor implements ScheduledExecutor {

    private final ScheduledExecutorService executor;

    public DefaultScheduledExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    private final Map<Future, CompletableFuture> relations = new ConcurrentHashMap<>();
    private final AtomicBoolean wasShutdownCalled = new AtomicBoolean(false);

    @Override
    public CompletableFuture<Void> schedule(Runnable command, long delay, TimeUnit unit) {
        Callable<Void> callable = () -> {
            command.run();
            return null;
        };
        return schedule(callable, delay, unit);
    }

    @Override
    public <T> CompletableFuture<T> schedule(Callable<T> command, long delay, TimeUnit unit) {
        CompletableFuture<T> wrapper = new CompletableFuture<>();
        try {
            Callable<T> callable = () -> {
                try {
                    T result = command.call();
                    wrapper.complete(result);
                    return result;
                } catch (Exception | Error e) {
                    wrapper.completeExceptionally(e);
                    throw e;
                }
            };
            Future<T> future = executor.schedule(callable, delay, unit);
            register(future, wrapper);
        } catch (Throwable e) {
            wrapper.completeExceptionally(e);
        }
        return wrapper;
    }

    private void register(Future future, CompletableFuture<?> wrapper) {
        relations.put(future, wrapper);
        wrapper.handle((result, throwable) -> {
            if (throwable instanceof CancellationException) {
                future.cancel(true);
            }
            relations.remove(future);
            return null;
        });
        if (wasShutdownCalled.get()) {
            wrapper.completeExceptionally(new RejectedExecutionException());
        }
    }

    @Override
    public void shutdownNow() {
        wasShutdownCalled.set(true);
        executor.shutdownNow();
        relations.values().stream().forEach(future -> future.completeExceptionally(new RejectedExecutionException()));
    }

    @Override
    public void shutdown() {
        wasShutdownCalled.set(true);
        executor.shutdown();
    }
}
