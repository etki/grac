package me.etki.grac.application;

import me.etki.grac.concurrent.CompletableFutures;

import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface ApplicationLevelInterceptor {

    default <I> CompletableFuture<ApplicationRequest<I>> processRequest(ApplicationRequest<I> request) {
        return CompletableFutures.completed(request);
    }

    default <O> CompletableFuture<ApplicationResponse<O>> processResponse(ApplicationResponse<O> response) {
        return CompletableFutures.completed(response);
    }
}
