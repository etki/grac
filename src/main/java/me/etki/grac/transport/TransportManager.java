package me.etki.grac.transport;

import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface TransportManager {

    CompletableFuture<TLResponse> execute(TLRequest request);
}
