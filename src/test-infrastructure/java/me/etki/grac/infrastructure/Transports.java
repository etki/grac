package me.etki.grac.infrastructure;

import me.etki.grac.infrastructure.transport.Mirror;
import me.etki.grac.infrastructure.transport.ResponseRepeater;
import me.etki.grac.transport.ResponseStatus;
import me.etki.grac.transport.ServerRequest;
import me.etki.grac.transport.ServerResponse;
import me.etki.grac.transport.Transport;
import me.etki.grac.transport.TransportRegistry;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Transports {

    public static TransportRegistry of(Transport... transports) {
        return new TransportRegistry(Arrays.asList(transports));
    }

    public static TransportRegistry empty() {
        return new TransportRegistry(new ArrayList<>());
    }

    public static Transport dummy() {
        return new Transport() {
            @Override
            public CompletableFuture<ServerResponse> execute(ServerRequest request) {
                throw new UnsupportedOperationException("This method should not be called");
            }

            @Override
            public boolean supports(String protocol) {
                return false;
            }
        };
    }

    public static Transport repeater(ServerResponse... responses) {
        return new ResponseRepeater(responses);
    }

    public static Transport mirror(Function<ServerRequest, ResponseStatus> statusConverter) {
        return new Mirror(statusConverter);
    }

    public static Transport mirror(ResponseStatus status) {
        return mirror(request -> status);
    }

    public static Transport mirror() {
        return mirror(ResponseStatus.OK);
    }

    public static Transport mock() {
        return Mockito.mock(Transport.class);
    }

    public static Transport mockForAnyProtocol() {
        Transport transport = mock();
        when(transport.supports(any())).thenReturn(true);
        return transport;
    }
}
