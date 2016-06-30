package me.etki.grac.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface CompletableFutureFactory {

    <T> CompletableFuture<T> supply(Supplier<T> supplier);
    CompletableFuture<Void> run(Runnable runnable);

    default <T> CompletableFuture<T> call(Callable<T> callable) {
        CompletableFuture<T> result = new CompletableFuture<>();
        run(() -> {
            try {
                result.complete(callable.call());
            } catch (Throwable e) {
                result.completeExceptionally(e);
            }
        })
        .exceptionally(throwable -> {
            result.completeExceptionally(throwable);
            return null;
        });
        return result;
    }

    default CompletableFuture<Void> execute(Task task) {
        return call(() -> {
            task.execute();
            return null;
        });
    }

    interface Task {
        void execute() throws Exception;
    }

    static <T> CompletableFuture<T> completedExceptionally(Throwable e) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }
}
