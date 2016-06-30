package me.etki.grac.concurrent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ScheduledExecutor {

    private final ScheduledExecutorService executor;

    public ScheduledExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    private final Map<Future, CompletableFuture> relations = new ConcurrentHashMap<>();
    private final AtomicBoolean wasShutdownCalled = new AtomicBoolean(false);

    public CompletableFuture<Void> schedule(Runnable command, long delay, TimeUnit unit) {
        Callable<Void> callable = () -> {
            command.run();
            return null;
        };
        return schedule(callable, delay, unit);
    }

    public <T> CompletableFuture<T> schedule(Callable<T> command, long delay, TimeUnit unit) {
        CompletableFuture<T> wrapper = new CompletableFuture<>();
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
            wrapper.cancel(true);
        }
    }

    public List<Runnable> shutdownNow() {
        wasShutdownCalled.set(true);
        List<Runnable> tasks = executor.shutdownNow();
        relations.values().stream().forEach(future -> future.cancel(true));
        return tasks;
    }
}
