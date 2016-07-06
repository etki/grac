package me.etki.grac.infrastructure.transport;

import me.etki.grac.transport.ServerRequest;
import me.etki.grac.transport.ServerResponse;
import me.etki.grac.transport.Transport;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class QueueTransport implements Transport {

    private final Queue<ServerResponse> queue;

    public QueueTransport(Queue<ServerResponse> queue) {
        this.queue = queue;
    }

    @Override
    public CompletableFuture<ServerResponse> execute(ServerRequest request) {
        return null;
    }

    @Override
    public boolean supports(String protocol) {
        return true;
    }
}
