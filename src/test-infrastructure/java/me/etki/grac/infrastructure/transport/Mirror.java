package me.etki.grac.infrastructure.transport;

import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.infrastructure.Responses;
import me.etki.grac.transport.ResponseStatus;
import me.etki.grac.transport.ServerRequest;
import me.etki.grac.transport.ServerResponse;
import me.etki.grac.transport.Transport;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Mirror implements Transport {

    private final Function<ServerRequest, ResponseStatus> statusConverter;

    public Mirror(Function<ServerRequest, ResponseStatus> statusConverter) {
        this.statusConverter = statusConverter;
    }

    @Override
    public CompletableFuture<ServerResponse> execute(ServerRequest request) {
        ServerResponse response = Responses.server(statusConverter.apply(request), request.getPayload().orElse(null),
                request.getMetadata());
        return CompletableFutures.completed(response);
    }

    @Override
    public boolean supports(String protocol) {
        return true;
    }
}
