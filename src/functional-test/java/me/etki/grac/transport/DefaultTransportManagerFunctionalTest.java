package me.etki.grac.transport;

import me.etki.grac.common.Action;
import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.concurrent.DelayService;
import me.etki.grac.infrastructure.Requests;
import me.etki.grac.infrastructure.Responses;
import me.etki.grac.infrastructure.ServerRegistries;
import me.etki.grac.policy.LinearBackoffRetryPolicy;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.policy.TimeRange;
import me.etki.grac.transport.server.ServerRegistry;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultTransportManagerFunctionalTest {

    @Test
    public void shouldRetryExpectedAmountOfTimes() throws Exception {
        RetryPolicy retryPolicy = new LinearBackoffRetryPolicy(new TimeRange(100, 400, 0), 5);
        TransportRequestExecutor executor = mock(TransportRequestExecutor.class);
        //noinspection unchecked
        when(executor.execute(any())).thenReturn(
                CompletableFutures.exceptional(new ConnectionException()),
                CompletableFutures.exceptional(new ConnectionException()),
                CompletableFutures.exceptional(new ConnectionException()),
                CompletableFutures.exceptional(new ConnectionException()),
                CompletableFutures.completed(Responses.server(ResponseStatus.OK))
        );
        DelayService delayService = mock(DelayService.class);
        when(delayService.await(anyLong(), any())).thenAnswer(mock -> CompletableFutures.completed(null));

        ServerRegistry servers = ServerRegistries.localhost();

        TransportManager transportManager = manager(servers, executor, delayService);
        TransportRequest request = Requests
                .transport(Action.READ, "/test", 1000L)
                .setRetryPolicy(retryPolicy);

        assertEquals(ResponseStatus.OK, transportManager.execute(request).get().getStatus());

        verify(executor, times(5)).execute(any());

        verify(delayService).await(eq(100L), eq(TimeUnit.MILLISECONDS));
        verify(delayService).await(eq(200L), eq(TimeUnit.MILLISECONDS));
        verify(delayService).await(eq(300L), eq(TimeUnit.MILLISECONDS));
        verify(delayService).await(eq(400L), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void shouldNotRetryNonIdempotentRequest() throws Exception {
        TransportRequestExecutor executor = mock(TransportRequestExecutor.class);
        //noinspection unchecked
        when(executor.execute(any())).thenReturn(
                CompletableFutures.exceptional(new ConnectionException()),
                CompletableFutures.completed(Responses.server(ResponseStatus.OK))
        );
        DelayService delayService = mock(DelayService.class);
        when(delayService.await(anyLong(), any())).thenAnswer(mock -> CompletableFutures.completed(null));

        ServerRegistry servers = ServerRegistries.localhost();

        TransportManager transportManager = manager(servers, executor, delayService);

        TransportRequest request = Requests.transport(Action.CREATE, "/test", 1000L);

        CompletableFuture<TransportResponse> response = transportManager.execute(request);
        response.exceptionally(t -> null).get();
        assertTrue(response.isCompletedExceptionally());

        verify(executor, times(1)).execute(any());
    }

    private static DefaultTransportManager manager(
            ServerRegistry servers,
            TransportRequestExecutor executor,
            DelayService delayService) {

        return new DefaultTransportManager(servers, executor, delayService, 32768);
    }
}
