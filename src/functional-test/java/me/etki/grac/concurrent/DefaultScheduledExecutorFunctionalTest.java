package me.etki.grac.concurrent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultScheduledExecutorFunctionalTest {

    public static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private ExecutorService secondaryExecutorService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        secondaryExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @After
    public void tearDown() {
        secondaryExecutorService.shutdownNow();
        secondaryExecutorService = null;
    }

    @Test(timeout = 1000)
    public void shouldCorrectlyShutdown() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
        DefaultScheduledExecutor executor = new DefaultScheduledExecutor(scheduledExecutorService);
        CompletableFutureFactory factory = new BasicCompletableFutureFactory(secondaryExecutorService);
        CompletableFuture<Void> synchronizer = new CompletableFuture<>();
        // epic hell
        List<CompletableFuture<Void>> futures = Stream.generate(() -> null)
                .limit(1024)
                .map(v ->
                        synchronizer
                                .thenCompose(vv ->
                                        factory
                                                .supply(() -> executor.schedule(() -> {}, 0, TimeUnit.SECONDS))
                                                .thenCompose(f -> f)
                                )
                )
                .collect(Collectors.toList());
        synchronizer.complete(null);
        executor.shutdownNow();
        for (CompletableFuture<Void> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                // ignore
            }
        }
        long doneFutures = futures.stream().filter(CompletableFuture::isDone).count();
        long cancelledFutures = futures.stream().filter(CompletableFuture::isCancelled).count();
        assertEquals(futures.size(), doneFutures + cancelledFutures);
    }

    @Test(timeout = 100)
    public void shouldCancelAllAfterShutdownRequests() {
        //expectedException.expectCause(isA(CancellationException.class));
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
        DefaultScheduledExecutor executor = new DefaultScheduledExecutor(scheduledExecutorService);
        executor.shutdownNow();
        CompletableFuture<Void> future = executor.schedule(() -> {}, 1, TimeUnit.SECONDS);
        assertTrue(future.isCompletedExceptionally());
    }
}
