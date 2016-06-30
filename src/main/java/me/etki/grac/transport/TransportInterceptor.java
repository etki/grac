package me.etki.grac.transport;

import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface TransportInterceptor {

    CompletableFuture<TransportRequest> processRequest(TransportRequest request);
    CompletableFuture<TransportResponse> processResponse(TransportResponse response);
}
