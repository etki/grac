package me.etki.grac.transport;

import me.etki.grac.common.Action;
import me.etki.grac.concurrent.DefaultScheduledExecutor;
import me.etki.grac.infrastructure.Servers;
import me.etki.grac.transport.server.DefaultServerRegistry;
import me.etki.grac.transport.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportInterceptorApplicationFunctionalTest {

    private DefaultScheduledExecutor scheduledExecutor;

    @Before
    public void setUp() {
        scheduledExecutor = new DefaultScheduledExecutor(Executors.newSingleThreadScheduledExecutor());
    }

    @After
    public void tearDown() {
        scheduledExecutor.shutdownNow();
    }

    @Test
    public void shouldApplyInterceptor() throws Exception {
        Server server = Servers.localhost();
        ServerRequest originalRequest = new ServerRequest()
                .setServer(server)
                .setAction(Action.CREATE)
                .setResource("/resource-a");
        ServerRequest requestReplacement = new ServerRequest()
                .setServer(server)
                .setAction(Action.DELETE)
                .setResource("/resource-b");
        ServerResponse originalResponse = new ServerResponse()
                .setStatus(ResponseStatus.OK);
        ServerResponse responseReplacement = new ServerResponse()
                .setStatus(ResponseStatus.CLIENT_ERROR);
        TransportInterceptor interceptor = mock(TransportInterceptor.class);
        when(interceptor.processRequest(originalRequest))
                .thenReturn(CompletableFuture.completedFuture(requestReplacement));
        when(interceptor.processResponse(originalResponse))
                .thenReturn(CompletableFuture.completedFuture(responseReplacement));

        Transport transport = mock(Transport.class);
        when(transport.supports(any())).thenReturn(true);
        when(transport.execute(requestReplacement)).thenReturn(CompletableFuture.completedFuture(originalResponse));
        TransportRegistry transports = new TransportRegistry(Collections.singletonList(transport));
        DefaultServerRegistry servers = mock(DefaultServerRegistry.class);
        when(servers.getServer()).thenReturn(CompletableFuture.completedFuture(Optional.of(server)));


        // todo

//        TransportRequestExecutor executor
//                = new TransportRequestExecutor(transports, servers, Collections.singletonList(interceptor));
//        ServerResponse response = executor.execute(originalRequest).get();
//        assertEquals(response, responseReplacement);
//        verify(interceptor, times(1)).processRequest(originalRequest);
//        verify(interceptor, times(1)).processResponse(originalResponse);
    }
}
