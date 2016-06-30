package me.etki.grac.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class BasicCompletableFutureFactory implements CompletableFutureFactory {

    private final Executor executor;

    public BasicCompletableFutureFactory(Executor executor) {
        this.executor = executor;
    }

    @Override
    public <T> CompletableFuture<T> supply(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    @Override
    public CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }
}
