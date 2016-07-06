package me.etki.grac.infrastructure;

import me.etki.grac.concurrent.TimeoutService;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Concurrency {

    public static <T> CompletableFuture<T> of(T value) {
        return CompletableFuture.completedFuture(value);
    }

    public static <T> CompletableFuture<T> of(Throwable throwable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(throwable);
        return future;
    }

    public static TimeoutService fakeTimeoutService() {
        TimeoutService timeoutService = mock(TimeoutService.class);
        when(timeoutService.setTimeout(any(), anyLong(), any()))
                .thenAnswer(mock -> new CompletableFuture<>());
        return timeoutService;
    }
}
