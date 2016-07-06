package me.etki.grac.transport.server;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface ServerProvider {

    CompletableFuture<List<Server>> get();
}
