package me.etki.grac.concurrent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.isA;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultTimeoutServiceFunctionalTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTimeoutServiceFunctionalTest.class);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ScheduledExecutor scheduledExecutor;

    @Before
    public void setUp() {
        scheduledExecutor = CustomExecutors.newSingleThreadScheduledExecutor();
    }

    @After
    public void tearDown() {
        scheduledExecutor.shutdownNow();
        scheduledExecutor = null;
    }

    @Test(timeout = 500)
    public void shouldTimelyFinishTargetCompletableFuture() throws Exception {
        expectedException.expectCause(isA(TimeoutException.class));

        TimeoutService service = new DefaultTimeoutService(scheduledExecutor);
        CompletableFuture<Void> infiniteFuture = new CompletableFuture<>();
        service.setTimeout(infiniteFuture, 10, TimeUnit.MILLISECONDS);
        infiniteFuture.get();
    }

    @Test(timeout = 500)
    public void shouldSuccessfullyCancelTimeout() throws Exception {
        TimeoutService service = new DefaultTimeoutService(scheduledExecutor);
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture<Void> timeout = service.setTimeout(future, 100, TimeUnit.MILLISECONDS);
        timeout.cancel(true);
        Thread.sleep(200);
        future.complete(null);
        future.get();
    }
}
