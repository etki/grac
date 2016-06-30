package me.etki.grac.transport;

import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface Transport {

    CompletableFuture<TransportResponse> execute(TransportRequest request);
    boolean supports(String protocol);
}
