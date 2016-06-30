package me.etki.grac;

import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface Interceptor {

    default <I> CompletableFuture<AssembledRequest<I>> processRequest(AssembledRequest<I> request) {
        return CompletableFuture.completedFuture(request);
    }

    default <I, O> CompletableFuture<AssembledResponse<I, O>> processResponse(AssembledResponse<I, O> response) {
        return CompletableFuture.completedFuture(response);
    }
}
