package me.etki.grac.transport;

import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface Transport {

    /**
     * @param request Request to execute.
     * @return Response promise. It is expected that promise will return either response, {@link ConnectionException}
     * if there was connection problem, {@link FaultyResponseException} if transport obtained invalid response, or
     * (discouraged) any other exception.
     */
    CompletableFuture<ServerResponse> execute(ServerRequest request);
    boolean supports(String protocol);
}
