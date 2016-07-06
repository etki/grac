package me.etki.grac.transport;

import me.etki.grac.common.Action;
import me.etki.grac.concurrent.TimeoutService;
import me.etki.grac.exception.NoSuitableTransportException;
import me.etki.grac.infrastructure.Concurrency;
import me.etki.grac.infrastructure.Requests;
import me.etki.grac.infrastructure.Servers;
import me.etki.grac.infrastructure.Transports;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class RequestExecutorFunctionalTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCorrectlyExecuteRequest() throws Exception {
        ServerResponse response = new ServerResponse()
                .setStatus(ResponseStatus.OK)
                .setDescription("all ok, no payload");
        ServerRequest request = new ServerRequest()
                .setServer(Servers.localhost())
                .setResource("/dummy")
                .setAction(Action.READ)
                .setTimeout(1000L);
        Transport transportA = mock(Transport.class);
        when(transportA.supports("http")).thenReturn(true);
        when(transportA.execute(any())).thenReturn(Concurrency.of(response));
        Transport transportB = mock(Transport.class);
        when(transportB.supports("http")).thenReturn(false);
        when(transportB.execute(any())).thenThrow(new RuntimeException());
        TimeoutService timeoutService = mock(TimeoutService.class);
        when(timeoutService.setTimeout(any(), anyLong(), any())).thenReturn(new CompletableFuture<>());
        TransportRequestExecutor executor = new TransportRequestExecutor(Transports.of(transportA), timeoutService);
        assertEquals(response, executor.execute(request).get());
    }

    @Test
    public void shouldReportMissingTransportWithNoSuitabletransportException() throws Exception {
        expectedException.expectCause(isA(NoSuitableTransportException.class));

        TimeoutService timeoutService = Concurrency.fakeTimeoutService();
        TransportRegistry transports = Transports.empty();
        TransportRequestExecutor executor = new TransportRequestExecutor(transports, timeoutService);
        executor.execute(Requests.server(Servers.localhost(), Action.CREATE, "/test", 1000L)).get();
    }
}
