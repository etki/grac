package me.etki.grac.transport.server;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface ServerRegistry {

    CompletableFuture<Optional<Server>> getServer();
}
