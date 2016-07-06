package me.etki.grac.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class CompletableFutures {

    private CompletableFutures() {
        // pure-static class
    }

    public static <T> CompletableFuture<T> exceptional(Throwable e) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }

    public static <T> CompletableFuture<T> completed(T value) {
        return CompletableFuture.completedFuture(value);
    }

    // todo rename
    public static <T> CompletableFuture<T> oneOf(T value, Throwable throwable) {
        if (value != null || throwable == null) {
            return CompletableFuture.completedFuture(value);
        }
        return exceptional(throwable);
    }

    /**
     * Executes callable and returns result/exception wrapped in completable future.
     *
     * Use with caution: while completable future is usually about delayed execution, this method executes instantly and
     * blocks if specified callable blocks.
     *
     * @param callable Callable to execute
     * @param <T> Return type
     * @return Callable result or caught exception wrapped in future
     */
    public static <T> CompletableFuture<T> wrap(Callable<T> callable) {
        try {
            return completed(callable.call());
        } catch (Throwable e) {
            return exceptional(e);
        }
    }
}
