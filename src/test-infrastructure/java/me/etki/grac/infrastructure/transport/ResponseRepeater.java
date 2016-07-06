package me.etki.grac.infrastructure.transport;

import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.transport.ServerRequest;
import me.etki.grac.transport.ServerResponse;
import me.etki.grac.transport.Transport;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ResponseRepeater implements Transport {

    private final ServerResponse[] responses;

    private final AtomicInteger iterator = new AtomicInteger(0);

    public ResponseRepeater(List<ServerResponse> responses) {
        this(responses.toArray(new ServerResponse[0]));
    }

    public ResponseRepeater(ServerResponse... responses) {
        this.responses = responses;
    }

    @Override
    public CompletableFuture<ServerResponse> execute(ServerRequest request) {
        if (responses.length == 0) {
            return CompletableFutures.exceptional(new NullPointerException());
        }
        return CompletableFutures.completed(responses[iterator.getAndIncrement() % responses.length]);
    }

    @Override
    public boolean supports(String protocol) {
        return true;
    }

    public ServerResponse[] getResponses() {
        return responses;
    }
}
